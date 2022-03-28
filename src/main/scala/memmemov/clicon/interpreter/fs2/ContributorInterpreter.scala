package memmemov.clicon.interpreter.fs2

import cats.effect.IO
import fs2.Stream as Fs2Stream
import cats.Applicative
import memmemov.clicon.algebra.{ContributorAlgebra, StreamAlgebra}
import memmemov.clicon.interpreter.fs2.symbol.{ContributorSymbol, StreamSymbol}
import memmemov.clicon.algebra.{StreamSymbol as AlgebraStreamSymbol,  ContributorSymbol as AlgebraContributorSymbol}

object ContributorInterpreter:

  def apply(): ContributorAlgebra[ContributorSymbol] =
    
    new ContributorAlgebra[ContributorSymbol]:

      override def createContributor(input: StreamSymbol, output: StreamSymbol): ContributorSymbol =
        ContributorSymbol(input, output)
