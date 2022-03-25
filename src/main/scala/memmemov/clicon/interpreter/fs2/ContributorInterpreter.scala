package memmemov.clicon.interpreter.fs2

import cats.effect.IO
import cats.Applicative
import memmemov.clicon.algebra.ContributorAlgebra
//import memmemov.clicon.algebra.symbol.{Contributor as AlgebraContributor, Stream as AlgebraStream}
import memmemov.clicon.interpreter.fs2.symbol.{Contributor, Stream}

object ContributorInterpreter:

  def apply[F[_] : Applicative](): ContributorAlgebra[F, Contributor, Stream] =
    
    new ContributorAlgebra[F, Contributor, Stream]:

      def createContributor(input: Stream, output: Stream): F[Contributor] =
        summon[Applicative[F]].pure(Contributor(input, output))
