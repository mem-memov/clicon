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

import memmemov.clicon.algebra._
import memmemov.clicon.algebra.symbol._
import memmemov.clicon.interpreter.fs2._

object Server extends IOApp {

  object ServerTest {

    val seconds: Stream[IO, FiniteDuration] = Stream.awakeEvery[IO](100.millis)

    case class Connection(from: Option[EntityBody[IO]])
    val connectionRefIO: IO[Ref[IO, Connection]] = Ref[IO].of(Connection(None))

    def buildRoutes(
      connectionRef: Ref[IO, Connection], 
      // transmissionRef: Ref[IO, TA.Transmission]
    ): HttpRoutes[IO] = {
      val dsl = Http4sDsl[IO]
      import dsl._

      HttpRoutes.of[IO] {
        case req @ POST -> Root / "from" =>
          for {
//            _ <- transmissionRef.update(transmission => transmission)
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

    def buildServer(connectionRef: Ref[IO, Connection]/*, transmissionRef: Ref[IO, TA.Transmission]*/) =
      EmberServerBuilder
        .default[IO]
        .withHttp2
        .withHost(ipv4"0.0.0.0")
        .withPort(port"8080")
        .withHttpApp(buildRoutes(connectionRef/*, transmissionRef*/).orNotFound)
        .build
  }

  def emptyTransmission()(using s: StreamAlgebra[OptionalByteStream], c: ContributorAlgebra[Contributor], t: TransmissionAlgebra[Transmission]) =
    import s._, c._, t._
    createTransmission(
      createContributor(
        useStream(None),
        useStream(None)
      ),
      createContributor(
        useStream(None),
        useStream(None)
      )
    )

  // def setContributor[T[_], Stream](transmission: T[algebra.Transmission] , contributor: T[algebra.Contributor])(using algebra: Algebra[T, Stream]): T[algebra.Transmission] = 
  //   import algebra._
  //   plugContributor(transmission, contributor)

  def run(args: List[String]): IO[ExitCode] =

    given streamInterpreter: StreamAlgebra[OptionalByteStream] = StreamInterpreter()
    given contributorInterpreter: ContributorAlgebra[Contributor] = ContributorInterpreter()
    given transmissionInterpreter: TransmissionAlgebra[Transmission] = TransmissionInterpreter()
    for {
//      emptyTransmission <- emptyTransmission()
//      transmissionRef <- Ref[IO].of(emptyTransmission)
      connectionRef <- ServerTest.connectionRefIO
      code <- ServerTest.buildServer(connectionRef/*, transmissionRef */).use(_ => IO.never).as(ExitCode.Success)
    } yield code
}
