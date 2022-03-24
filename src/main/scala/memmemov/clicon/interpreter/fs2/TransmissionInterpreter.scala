package memmemov.clicon.interpreter.fs2

import memmemov.clicon.algebra.{ContributorAlgebra, TransmissionAlgebra}
import memmemov.clicon.algebra.symbol.{Contributor, Transmission}

object TransmissionInterpreter:

  def apply()(using ContributorAlgebra[Contributor]): TransmissionAlgebra[Transmission] =

    new TransmissionAlgebra[Transmission]:

      def createTransmission(initiator: Contributor, approver: Contributor): Transmission = Transmission(initiator, approver)

      def plugContributor(transmission: Transmission, contributor: Contributor): Transmission =
        transmission match
          case Transmission(Contributor(None, None), approver) => createTransmission(contributor, approver)
          case Transmission(initiator, Contributor(None, None)) => createTransmission(initiator, contributor)
          case _ => ???

      def unplugContributor(transmission: Transmission, contributor: Contributor): Transmission = ???
