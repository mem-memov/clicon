package memmemov.clicon.interpreter.fs2

import cats.effect.IO
import fs2.Stream as Fs2Stream
import cats.Applicative
import memmemov.clicon.algebra
import memmemov.clicon.interpreter.fs2.R
import memmemov.clicon.algebra.symbol

object ContributorInterpreter:

  def apply(): algebra.Contributor[R] =
    
    new algebra.Contributor[R]:

      override def createContributor(input: R[symbol.Stream], output: R[symbol.Stream]): R[symbol.Contributor] =
        R(symbol.Contributor(input.value, output.value))
