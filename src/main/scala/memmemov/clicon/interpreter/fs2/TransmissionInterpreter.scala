package memmemov.clicon.interpreter.fs2

import cats.Applicative
import memmemov.clicon.algebra
import memmemov.clicon.algebra.symbol
import memmemov.clicon.interpreter.fs2.R
import memmemov.clicon.interpreter.fs2

object TransmissionInterpreter:

  def apply(): algebra.Transmission[R] =

    new algebra.Transmission[R]:

      override def createTransmission(): R[symbol.Transmission] =
        R(symbol.Transmission(Option.empty, Option.empty))

      override def plugContributor(transmission: R[symbol.Transmission], contributor: R[symbol.Contributor]): R[symbol.Transmission] =
        val value = transmission.value match
          case symbol.Transmission(None, approver) => symbol.Transmission(Option(contributor.value), approver)
          case symbol.Transmission(initiator, None) => symbol.Transmission(initiator, Option(contributor.value))
          case _ => ???
        R(value)

      override def unplugContributor(transmission: R[symbol.Transmission], contributor: R[symbol.Contributor]): R[symbol.Transmission] = ???
