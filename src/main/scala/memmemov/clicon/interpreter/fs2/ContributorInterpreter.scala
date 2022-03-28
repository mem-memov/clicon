package memmemov.clicon.interpreter.fs2

import cats.effect.IO
import fs2.Stream as Fs2Stream
import cats.Applicative
import memmemov.clicon.algebra.{ContributorAlgebra, StreamAlgebra}
import memmemov.clicon.interpreter.fs2.R
import memmemov.clicon.interpreter.fs2.symbol.{ContributorSymbol, StreamSymbol}
import memmemov.clicon.algebra.symbol as AS

object ContributorInterpreter:

  def apply(): ContributorAlgebra[R] =
    
    new ContributorAlgebra[R]:

      override def createContributor(input: R[AS.StreamSymbol], output: R[AS.StreamSymbol]): R[AS.ContributorSymbol] =
        R(ContributorSymbol(input.value, output.value))
