package memmemov.clicon.transmission

import memmemov.clicon.transmission.Algebra
import cats.effect.IO
import cats.Monad

given ioAlgebra[F[_]: Monad, S]: Algebra[F] = new Algebra[F]:

  type Stream = Option[S]

  case class Contributor(input: Stream, output: Stream)

  case class Transmission(initiator: Contributor, approver: Contributor)

  override def stream(stream: Stream): F[Stream] = 
    summon[F[Stream]].pure(stream)

  override def contributor(input: F[Stream], output: F[Stream]): F[Contributor] = 
    for {
      i <- input
      o <- output
    } yield Contributor(i, o)

  // override def transmission(initiator: F[Contributor], approver: F[Contributor]): F[Transmission] = 
  //   for {
  //     i <- initiator
  //     a <- approver
  //   } yield Transmission(i, a)

  // override def plugContributor(transmission: F[Transmission], contributor: F[Contributor]): F[Transmission] = 
  //   transmission.value match
  //     case Transmission(Contributor(None, None), approver) => IO(Transmission(contributor.value, approver))
  //     case Transmission(initiator, Contributor(None, None)) => IO(Transmission(initiator, contributor.value))
  //     case _ => ???

  // override def unplugContributor(transmission: F[Transmission], contributor: F[Contributor]): F[Transmission] = ???
