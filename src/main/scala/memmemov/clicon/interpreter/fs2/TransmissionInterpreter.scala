package memmemov.clicon.interpreter.fs2

import cats.Applicative
import memmemov.clicon.algebra.{ContributorAlgebra, TransmissionAlgebra}
import memmemov.clicon.interpreter.fs2.symbol.{ContributorSymbol, TransmissionSymbol}
import memmemov.clicon.interpreter.fs2.R

object TransmissionInterpreter:

  def apply(): TransmissionAlgebra[TransmissionSymbol] =

    new TransmissionAlgebra[TransmissionSymbol]:

      override def createTransmission(): TransmissionSymbol =
        TransmissionSymbol(Option.empty, Option.empty)

      override def plugContributor(transmission: TransmissionSymbol, contributor: ContributorSymbol): TransmissionSymbol =
        transmission match
          case TransmissionSymbol(None, approver) => TransmissionSymbol(Option(contributor), approver)
          case TransmissionSymbol(initiator, None) => TransmissionSymbol(initiator, Option(contributor))
          case _ => ???

      override def unplugContributor(transmission: TransmissionSymbol, contributor: ContributorSymbol): TransmissionSymbol = ???
