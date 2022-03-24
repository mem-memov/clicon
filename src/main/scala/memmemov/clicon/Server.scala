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

//import memmemov.clicon.transmission as TA

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

//  def emptyTransmission =
//    i.transmission(
//      i.contributor(
//        i.stream(Option.empty[Stream]),
//        i.stream(Option.empty[Stream])
//      ),
//      i.contributor(
//        i.stream(Option.empty[Stream]),
//        i.stream(Option.empty[Stream])
//      )
//    )

  // def setContributor[T[_], Stream](transmission: T[algebra.Transmission] , contributor: T[algebra.Contributor])(using algebra: Algebra[T, Stream]): T[algebra.Transmission] = 
  //   import algebra._
  //   plugContributor(transmission, contributor)

  def run(args: List[String]): IO[ExitCode] = 
//    import memmemov.clicon.transmission.interpreter._
//    val transmissionInterpreter = IOByteStreamInterpreter.make()
    for {
//      emptyTransmission <- emptyTransmission[IO, Stream[IO, Byte]](transmissionInterpreter)
//      transmissionRef <- Ref[IO].of(emptyTransmission)
      connectionRef <- ServerTest.connectionRefIO
      code <- ServerTest.buildServer(connectionRef/*, transmissionRef */).use(_ => IO.never).as(ExitCode.Success)
    } yield code
}
