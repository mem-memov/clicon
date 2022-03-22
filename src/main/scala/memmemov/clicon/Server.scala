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
    val connectionRef: IO[Ref[IO, Connection]] = Ref[IO].of(Connection(None))

    def directorRoutes: HttpRoutes[IO] = {
      val dsl = Http4sDsl[IO]
      import dsl._

      HttpRoutes.of[IO] {
        case req @ POST -> Root / "from" =>
          for {
            _ <- IO(println("from stream acquired"))
            connection <- connectionRef
            _ <- connection.update(_ => Connection(Option(req.body)))
            result <- Ok(seconds.map(_.toString))
          } yield result
//          Ok(seconds.map(_.toString))


        case req @ POST -> Root / "to" =>
          for {
            connection <- connectionRef
            c <- connection.get
            result <- Ok(c.from match {
              case None => seconds.map(_.toString)
              case Some(bodyStream) => bodyStream.map(_.toString)
            })
          } yield result
      }
    }

    def testCleartext =
      EmberServerBuilder
        .default[IO]
        .withHttp2
        .withHost(ipv4"0.0.0.0")
        .withPort(port"8080")
        .withHttpApp(directorRoutes.orNotFound)
        .build
  }

  def run(args: List[String]): IO[ExitCode] =
    ServerTest
      .testCleartext
      .use(_ => IO.never)
      .as(ExitCode.Success)
}
