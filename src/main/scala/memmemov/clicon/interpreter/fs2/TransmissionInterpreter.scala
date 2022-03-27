package memmemov.clicon.interpreter.fs2

import cats.Applicative
import memmemov.clicon.algebra.{ContributorAlgebra, TransmissionAlgebra}
import memmemov.clicon.interpreter.fs2.symbol.{ContributorSymbol, TransmissionSymbol}
import memmemov.clicon.interpreter.fs2.R

object TransmissionInterpreter:

  def apply(): TransmissionAlgebra[R] =

    new TransmissionAlgebra[R]:

      override type Contributor = ContributorSymbol
      override opaque type Transmission = TransmissionSymbol

      override def createTransmission(): R[Transmission] =
        R(TransmissionSymbol(Option.empty, Option.empty))

      override def plugContributor(transmission: R[Transmission], contributor: R[Contributor]): R[Transmission] =
        val value = transmission.value match
          case TransmissionSymbol(None, approver) => TransmissionSymbol(Option(contributor.value), approver)
          case TransmissionSymbol(initiator, None) => TransmissionSymbol(initiator, Option(contributor.value))
          case _ => ???
        R(value)

      override def unplugContributor(transmission: R[Transmission], contributor: R[Contributor]): R[Transmission] = ???
