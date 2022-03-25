package memmemov.clicon.interpreter.fs2

import cats.Applicative
import memmemov.clicon.algebra.TransmissionAlgebra
import memmemov.clicon.interpreter.fs2.symbol.{Contributor, Transmission}

object TransmissionInterpreter:

  def apply[F[_] : Applicative](): TransmissionAlgebra[F, Transmission, Contributor] =

    new TransmissionAlgebra[F, Transmission, Contributor]:

      def createTransmission(): F[Transmission] =
        summon[Applicative[F]].pure(Transmission(Option.empty, Option.empty))

      def plugContributor(transmission: Transmission, contributor: Contributor): F[Transmission] =
        val newTransmission = transmission match
          case Transmission(None, approver) => Transmission(Option(contributor), approver)
          case Transmission(initiator, None) => Transmission(initiator, Option(contributor))
          case _ => ???
        summon[Applicative[F]].pure(newTransmission)

      def unplugContributor(transmission: Transmission, contributor: Contributor): F[Transmission] = ???
