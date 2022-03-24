package memmemov.clicon.transmission

import memmemov.clicon.transmission.Algebra
import cats.effect.IO

object AlgebraIO:
  
  given [Stream]: Algebra[IO, Stream] = new Algebra[IO, Stream]:

    case class Contributor(input: Option[Stream], output: Option[Stream])

    case class Transmission(initiator: Contributor, approver: Contributor)

    override def stream(stream: Option[Stream]): IO[Option[Stream]] = 
      IO(stream)

    override def contributor(inputIO: IO[Option[Stream]], outputIO: IO[Option[Stream]]): IO[Contributor] = 
      for {
        i <- inputIO
        o <- outputIO
      } yield Contributor(i, o)

    override def transmission(initiatorIO: IO[Contributor], approverIO: IO[Contributor]): IO[Transmission] = 
      for {
        i <- initiatorIO
        a <- approverIO
      } yield Transmission(i, a)

    override def plugContributor(transmissionIO: IO[Transmission], contributorIO: IO[Contributor]): IO[Transmission] = 
      for {
        t <- transmissionIO
        _ <- t match
          case Transmission(Contributor(None, None), approver) => transmission(contributorIO, IO(approver))
          case Transmission(initiator, Contributor(None, None)) => transmission(IO(initiator), contributorIO)
          case _ => ???
      } yield t

    override def unplugContributor(transmission: IO[Transmission], contributor: IO[Contributor]): IO[Transmission] = ???



