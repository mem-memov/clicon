package memmemov.clicon.interpreter.fs2.symbol


import memmemov.clicon.algebra.ContributorAlgebra
import memmemov.clicon.algebra.{StreamSymbol as AlgebraStreamSymbol,  ContributorSymbol as AlgebraContributorSymbol, TransmissionSymbol as AlgebraTransmissionSymbol}

case class TransmissionSymbol(initiator: Option[ContributorSymbol], approver: Option[ContributorSymbol]) extends AlgebraTransmissionSymbol