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
import org.http4s.*
import org.http4s.dsl.io.*

import cats.effect.std.Queue

import java.util.UUID
import memmemov.clicon.algebra
import memmemov.clicon.algebra.symbol
import memmemov.clicon.interpreter.fs2.*
import memmemov.clicon.interpreter.fs2.R as Repr
import memmemov.clicon.interpreter.fs2.symbol.StreamSymbol

object Server extends IOApp {

  object ServerTest {

    val seconds: Stream[IO, FiniteDuration] = Stream.awakeEvery[IO](10.millis)

    case class Connection(from: Option[EntityBody[IO]])
    val connectionRefIO: IO[Ref[IO, Connection]] = Ref[IO].of(Connection(None))



    def buildRoutes[R[_]: algebra.Transmission : algebra.Stream : algebra.Contributor](
      connectionRef: Ref[IO, Connection], 
      transmissionRef: Ref[IO, R[symbol.Transmission]],
      forwardQueue: Queue[IO, Option[Byte]],
      forwardStream: Stream[IO, Byte],
      backwardQueue: Queue[IO, Option[Byte]],
      backwardStream: Stream[IO, Byte]
    ): HttpRoutes[IO] = {
      val dsl = Http4sDsl[IO]
      import dsl._

      HttpRoutes.of[IO] {
        case req @ POST -> Root / "from" =>
          for {
            t <- transmissionRef.get
            newT <- IO(plug(t, StreamSymbol(Stream.never), StreamSymbol(req.body)))
            _ <- transmissionRef.update(_ => newT)

            _ <- IO(println("FROM started"))
            fw <- IO(req.body.map(byte => Option(byte)).evalTap{ ob =>
              for {
                _ <- forwardQueue.offer(ob)
                _ <- IO(println(s"[from] $ob"))
              } yield ()
            }.collect {
              case Some(v) => v
            })

            result <- Ok(fw)

//            connection <- connectionRef.updateAndGet(_ => Connection(Option(req.body)))
//            result <- IO.sleep(10.second) >> Ok(req.body)
          } yield result

        case req @ POST -> Root / "to" =>
          for {
//            t <- transmissionRef.get
//            newT <- IO(plug(t, StreamSymbol(Stream.never), StreamSymbol(req.body)))
//            _ <- transmissionRef.update(_ => newT)

            _ <- IO(println("TO started"))
//            _ <- IO(req.body.map(byte => Option(byte)).evalTap{ ob =>
//              for {
//                _ <- backwardQueue.offer(ob)
//                _ <- IO(println(s"[to] $ob"))
//              } yield ()
//            })

            result <- Ok(forwardStream)

//            connection <- connectionRef.get
//            result <- connection.from match {
//              case None => Ok(seconds.map(_ => "NOTHING "))
//              case Some(bodyStream) => Ok(bodyStream)
//            }
          } yield result

      }
    }

    def buildServer[
      R[_]: algebra.Transmission : algebra.Stream : algebra.Contributor
    ](
      connectionRef: Ref[IO, Connection],
      transmissionRef: Ref[IO, R[symbol.Transmission]],
      forwardQueue: Queue[IO, Option[Byte]],
      forwardStream: Stream[IO, Byte],
      backwardQueue: Queue[IO, Option[Byte]],
      backwardStream: Stream[IO, Byte]
    ) =
      EmberServerBuilder
        .default[IO]
        .withHttp2
        .withHost(ipv4"0.0.0.0")
        .withPort(port"8080")
        .withHttpApp(buildRoutes(
          connectionRef,
          transmissionRef,
          forwardQueue,
          forwardStream,
          backwardQueue,
          backwardStream
        ).orNotFound)
        .build
  }


  def plug[
    R[_]: algebra.Transmission : algebra.Contributor : algebra.Stream
  ](
    transmission: R[symbol.Transmission],
    input: symbol.Stream,
    output: symbol.Stream
  ) =
    val tDsl = summon[algebra.Transmission[R]]
    val cDsl = summon[algebra.Contributor[R]]
    val sDsl = summon[algebra.Stream[R]]
    import tDsl._, cDsl._, sDsl._

    plugContributor(
      transmission,
      createContributor(
        useStream(input),
        useStream(output)
      )
    )


  def run(args: List[String]): IO[ExitCode] =

    given streamInterpreter: algebra.Stream[Repr] = StreamInterpreter()
    given contributorInterpreter: algebra.Contributor[Repr] = ContributorInterpreter()
    given transmissionInterpreter: algebra.Transmission[Repr] = TransmissionInterpreter()
    import transmissionInterpreter._
    val transmission = createTransmission()
    for {
      transmissionRef <- Ref[IO].of(transmission)

      forwardQueue <- Queue.unbounded[IO, Option[Byte]]
      forwardStream <- IO(Stream.fromQueueNoneTerminated(forwardQueue))
      backwardQueue <- Queue.unbounded[IO, Option[Byte]]
      backwardStream <- IO(Stream.fromQueueNoneTerminated(forwardQueue))

      connectionRef <- ServerTest.connectionRefIO
      code <- ServerTest.buildServer[R](
        connectionRef,
        transmissionRef,
        forwardQueue,
        forwardStream,
        backwardQueue,
        backwardStream
      ).use(_ => IO.never).as(ExitCode.Success)
    } yield code
}
