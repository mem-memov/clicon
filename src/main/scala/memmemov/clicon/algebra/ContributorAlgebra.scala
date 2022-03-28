package memmemov.clicon.algebra

import memmemov.clicon.algebra.symbol.{ContributorSymbol, StreamSymbol}

trait ContributorAlgebra[R[_]]:

  def createContributor(input: R[StreamSymbol], output: R[StreamSymbol]): R[ContributorSymbol]
