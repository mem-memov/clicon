package memmemov.clicon.interpreter.fs2

import cats.Applicative
import memmemov.clicon.algebra.{ContributorAlgebra, TransmissionAlgebra, symbol}
import memmemov.clicon.interpreter.fs2.R
import memmemov.clicon.algebra.symbol.ContributorSymbol
import memmemov.clicon.interpreter.fs2
import memmemov.clicon.interpreter.fs2.symbol.TransmissionSymbol

object TransmissionInterpreter:

  def apply(): TransmissionAlgebra[R] =

    new TransmissionAlgebra[R]:

      override def createTransmission(): R[symbol.TransmissionSymbol] =
        R(TransmissionSymbol(Option.empty, Option.empty))

      override def plugContributor(transmission: R[symbol.TransmissionSymbol], contributor: R[ContributorSymbol]): R[symbol.TransmissionSymbol] =
        val value = transmission.value match
          case TransmissionSymbol(None, approver) => fs2.symbol.TransmissionSymbol(Option(contributor.value), approver)
          case TransmissionSymbol(initiator, None) => fs2.symbol.TransmissionSymbol(initiator, Option(contributor.value))
          case _ => ???
        R(value)

      override def unplugContributor(transmission: R[symbol.TransmissionSymbol], contributor: R[ContributorSymbol]): R[symbol.TransmissionSymbol] = ???
