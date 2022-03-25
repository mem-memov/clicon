package memmemov.clicon

import cats.*
import cats.effect.*
import cats.syntax.all.*
import com.comcast.ip4s.*
import fs2.*
import org.http4s.*
import org.http4s.dsl.*
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits.*

import scala.concurrent.duration.*
import cats.effect.*
import fs2.Stream
import memmemov.clicon.interpreter.fs2.StreamInterpreter
import org.http4s.*
import org.http4s.dsl.io.*

import java.util.UUID
import memmemov.clicon.algebra.*
import memmemov.clicon.interpreter.fs2.*
import memmemov.clicon.interpreter.fs2.symbol.{Contributor as ContributorSymbol, Stream as StreamSymbol, Transmission as TransmissionSymbol}

object Server extends IOApp {

  object ServerTest {

    val seconds: Stream[IO, FiniteDuration] = Stream.awakeEvery[IO](100.millis)

    case class Connection(from: Option[EntityBody[IO]])
    val connectionRefIO: IO[Ref[IO, Connection]] = Ref[IO].of(Connection(None))

    def buildRoutes(
      connectionRef: Ref[IO, Connection], 
      transmissionRef: Ref[IO, TransmissionSymbol]
    ): HttpRoutes[IO] = {
      val dsl = Http4sDsl[IO]
      import dsl._

      val streamInterpreter: StreamAlgebra[IO, StreamSymbol] = StreamInterpreter()
      val contributorInterpreter: ContributorAlgebra[IO, ContributorSymbol, StreamSymbol] = ContributorInterpreter()
      val transmissionInterpreter: TransmissionAlgebra[IO, TransmissionSymbol, ContributorSymbol] = TransmissionInterpreter()
      import streamInterpreter._, contributorInterpreter._, transmissionInterpreter._

      HttpRoutes.of[IO] {
        case req @ POST -> Root / "from" =>
          for {
            t <- transmissionRef.get
            in <- useStream(StreamSymbol(req.body))
            out <- useStream(StreamSymbol(req.body))
            c <- createContributor(in, out)
            newT <- plugContributor(t, c)
            _ <- transmissionRef.update(_ => newT)

            connection <- connectionRef.updateAndGet(_ => Connection(Option(req.body)))
            result <- IO.sleep(10.second) >> Ok(req.body)
          } yield result

        case req @ POST -> Root / "to" =>
          for {
            connection <- connectionRef.get
            result <- connection.from match {
              case None => Ok(seconds.map(_ => "NOTHING "))
              case Some(bodyStream) => Ok(bodyStream)
            }
          } yield result
      }
    }

    def buildServer(connectionRef: Ref[IO, Connection], transmissionRef: Ref[IO, TransmissionSymbol]) =
      EmberServerBuilder
        .default[IO]
        .withHttp2
        .withHost(ipv4"0.0.0.0")
        .withPort(port"8080")
        .withHttpApp(buildRoutes(connectionRef, transmissionRef).orNotFound)
        .build
  }

  def run(args: List[String]): IO[ExitCode] =

    val streamInterpreter: StreamAlgebra[IO, StreamSymbol] = StreamInterpreter()
    val contributorInterpreter: ContributorAlgebra[IO, ContributorSymbol, StreamSymbol] = ContributorInterpreter()
    val transmissionInterpreter: TransmissionAlgebra[IO, TransmissionSymbol, ContributorSymbol] = TransmissionInterpreter()
    import transmissionInterpreter._
    for {
      transmission <- createTransmission()
      transmissionRef <- Ref[IO].of(transmission)
      connectionRef <- ServerTest.connectionRefIO
      code <- ServerTest.buildServer(connectionRef, transmissionRef).use(_ => IO.never).as(ExitCode.Success)
    } yield code
}
