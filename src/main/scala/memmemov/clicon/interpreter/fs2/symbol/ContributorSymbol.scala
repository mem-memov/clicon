package memmemov.clicon.interpreter.fs2.symbol

import memmemov.clicon.algebra.StreamAlgebra
import memmemov.clicon.algebra.{StreamSymbol as AlgebraStreamSymbol,  ContributorSymbol as AlgebraContributorSymbol}

case class ContributorSymbol(input: StreamSymbol, output: StreamSymbol) extends AlgebraContributorSymbol
