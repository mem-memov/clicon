package memmemov.clicon

import cats._
import cats.effect._
import cats.syntax.all._
import com.comcast.ip4s._
import fs2._
import org.http4s._
import org.http4s.dsl._
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits._
import scala.concurrent.duration._

import cats.effect._
import fs2.Stream
import org.http4s._
import org.http4s.dsl.io._

import java.util.UUID



object Server extends IOApp {

  object ServerTest {

    val seconds: Stream[IO, FiniteDuration] = Stream.awakeEvery[IO](100.millis)

    case class Connection(from: Option[EntityBody[IO]])
    val connectionRefIO: IO[Ref[IO, Connection]] = Ref[IO].of(Connection(None))

    def buildRoutes(connectionRef: Ref[IO, Connection]): HttpRoutes[IO] = {
      val dsl = Http4sDsl[IO]
      import dsl._

      HttpRoutes.of[IO] {
        case req @ POST -> Root / "from" =>
          for {
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

    def buildServer(connectionRef: Ref[IO, Connection]) =
      EmberServerBuilder
        .default[IO]
        .withHttp2
        .withHost(ipv4"0.0.0.0")
        .withPort(port"8080")
        .withHttpApp(buildRoutes(connectionRef).orNotFound)
        .build
  }

  def run(args: List[String]): IO[ExitCode] = (for {
    connectionRef <- ServerTest.connectionRefIO
    code <- ServerTest.buildServer(connectionRef).use(_ => IO.never).as(ExitCode.Success)
  } yield code)
}
