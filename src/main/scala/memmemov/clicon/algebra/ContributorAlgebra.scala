package memmemov.clicon.algebra

trait ContributorAlgebra[F[_], Contributor, Stream]:

  def createContributor(input: Stream, output: Stream): F[Contributor]
