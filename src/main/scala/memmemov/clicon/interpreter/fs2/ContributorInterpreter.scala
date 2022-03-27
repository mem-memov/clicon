package memmemov.clicon.interpreter.fs2

import cats.effect.IO
import fs2.Stream as Fs2Stream
import cats.Applicative
import memmemov.clicon.algebra.{ContributorAlgebra, StreamAlgebra}
import memmemov.clicon.interpreter.fs2.symbol.ContributorSymbol
import memmemov.clicon.interpreter.fs2.R

object ContributorInterpreter:

  def apply(): ContributorAlgebra[R] =
    
    new ContributorAlgebra[R]:

      override type Stream = Fs2Stream[IO, Byte]
      override type Contributor = ContributorSymbol

      override def createContributor(input: R[Stream], output: R[Stream]): R[Contributor] =
        R(ContributorSymbol(input.value, output.value))
