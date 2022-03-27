package memmemov.clicon.algebra

trait ContributorAlgebra[R[_]]:
  
  type Stream
  type Contributor

  def createContributor(input: R[Stream], output: R[Stream]): R[Contributor]
