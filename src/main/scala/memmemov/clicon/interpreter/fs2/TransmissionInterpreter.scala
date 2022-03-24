package memmemov.clicon.interpreter.fs2

import memmemov.clicon.algebra.{ContributorAlgebra, TransmissionAlgebra}
import memmemov.clicon.algebra.symbol.{Contributor, Transmission}

object TransmissionInterpreter:

  def apply(): TransmissionAlgebra[Transmission] =

    new TransmissionAlgebra[Transmission]:

      def transmission(initiator: Contributor, approver: Contributor): Transmission = Transmission(initiator, approver)

      def plugContributor(transmissionP: Transmission, contributor: Contributor): Transmission =
        transmissionP match
          case Transmission(Contributor(None, None), approver) => transmission(contributor, approver)
          case Transmission(initiator, Contributor(None, None)) => transmission(initiator, contributor)
          case _ => ???

      def unplugContributor(transmissionP: Transmission, contributor: Contributor): Transmission = ???
