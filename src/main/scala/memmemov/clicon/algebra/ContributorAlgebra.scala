package memmemov.clicon.algebra

trait ContributorAlgebra[Contributor]:
  
  type Stream

  def createContributor(input: Stream, output: Stream): Contributor
