package memmemov.clicon.algebra

import memmemov.clicon.algebra.symbol.OptionalByteStream

trait ContributorAlgebra[C]:

  def createContributor(input: OptionalByteStream, output: OptionalByteStream): C
