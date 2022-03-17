package memmemov.clicon

// https://blog.rockthejvm.com/http4s-tutorial/

import cats.*
import cats.effect.*
import cats.implicits.*
import org.http4s.circe.*
import org.http4s.*
import io.circe.generic.auto.*
import io.circe.syntax.*
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.dsl.*
import org.http4s.dsl.impl.*
import org.http4s.headers.*
import org.http4s.implicits.*
import org.http4s.server.*

import java.time.Year
import java.util.UUID
import scala.util.Try
import scala.collection.mutable

object Http4sTutorial extends IOApp {

  type Actor = String

  case class Movie(id: String, title: String, year: Int, actors: List[String], director: String)

  case class Director(firstName: String, lastName: String) {
    override def toString: String = s"$firstName $lastName"
  }

  case class DirectorDetails(firstName: String, lastName: String, genre: String)

  val snjl: Movie = Movie(
    "6bcbca1e-efd3-411d-9f7c-14b872444fce",
    "Zack Snyder's Justice League",
    2021,
    List("Henry Cavill", "Gal Godot", "Ezra Miller", "Ben Affleck", "Ray Fisher", "Jason Momoa"),
    "Zack Snyder"
  )

  val movies: Map[String, Movie] = Map(snjl.id -> snjl)

  private def findMovieById(movieId: UUID) =
    movies.get(movieId.toString)

  private def findMoviesByDirector(director: String): List[Movie] =
    movies.values.filter(_.director == director).toList

  def movieRoutes[F[_] : Monad]: HttpRoutes[F] = {
    val dsl = Http4sDsl[F]
    import dsl._

    given QueryParamDecoder[Year] = QueryParamDecoder[Int].emap { yearInt =>
      Try(Year.of(yearInt)).toEither.leftMap { e =>
        ParseFailure(e.getMessage, e.getMessage)
      }
    }

    object DirectorQueryParamMatcher extends QueryParamDecoderMatcher[String]("director")
    object YearQueryParamMatcher extends OptionalValidatingQueryParamDecoderMatcher[Year]("year")

    HttpRoutes.of[F] {
      case GET -> Root / "movies" :? DirectorQueryParamMatcher(director) +& YearQueryParamMatcher(maybeYear) =>
        val moviesByDirector = findMoviesByDirector(director)
        maybeYear match {
          case Some(validatedYear) =>
            validatedYear.fold (
              _ => BadRequest("The year was badly formatted"),
              year => {
                val moviesByDirectorAndYear = moviesByDirector.filter(_.year == year.getValue)
                Ok(moviesByDirectorAndYear.asJson)
              }
            )
          case None => Ok(moviesByDirector.asJson)
        }
      case GET -> Root / "movies" / UUIDVar(movieId) / "actors" =>
        findMovieById(movieId).map(_.actors) match {
          case Some(actors) => Ok(actors.asJson)
          case None => NotFound(s"No movie with $movieId found in the database")
        }
    }
  }

  val directorDetailsDb: mutable.Map[Director, DirectorDetails] = mutable.Map(
    Director("Zack", "Snider") -> DirectorDetails("Zack", "Snider", "superhero")
  )

  def directorRoutes[F[_] : Monad]: HttpRoutes[F] = {
    val dsl = Http4sDsl[F]
    import dsl._

    object DirectorVar {
      def unapply(str: String): Option[Director] = {
        Try {
          val tokens = str.split(' ')
          Director(tokens(0), tokens(1))
        }.toOption
      }
    }

    HttpRoutes.of[F] {
      case GET -> Root / "directors" / DirectorVar(director) =>
        directorDetailsDb.get(director) match {
          case Some(dirDetails) => Ok(dirDetails.asJson)
          case _ => NotFound(s"No director '$director' found")
        }
    }
  }

  def allRoutes[F[_] : Monad]: HttpRoutes[F] = movieRoutes[F] <+> directorRoutes[F]

  def allRoutesComplete[F[_]: Monad]: HttpApp[F] = allRoutes[F].orNotFound

  override def run(args: List[String]): IO[ExitCode] = {
//    val apis = Router(
//      "/api" -> movieRoutes[IO],
//      "/api/admin" -> directorRoutes[IO]
//    ).orNotFound

    BlazeServerBuilder[IO](runtime.compute)
      .bindHttp(8080, "localhost")
      .withHttpApp(allRoutesComplete)
      .resource
      .use(_ => IO.never)
      .as(ExitCode.Success)
  }
}

// http://localhost:8080/movies?director=Zack%20Snyder&year=2021
