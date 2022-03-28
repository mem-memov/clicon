package memmemov.clicon.algebra

trait ContributorAlgebra[Contributor]:

  def createContributor(input: StreamSymbol, output: StreamSymbol): Contributor
