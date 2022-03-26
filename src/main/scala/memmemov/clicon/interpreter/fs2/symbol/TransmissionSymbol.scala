package memmemov.clicon.interpreter.fs2.symbol


import memmemov.clicon.algebra.ContributorAlgebra

case class TransmissionSymbol(initiator: Option[ContributorSymbol], approver: Option[ContributorSymbol])