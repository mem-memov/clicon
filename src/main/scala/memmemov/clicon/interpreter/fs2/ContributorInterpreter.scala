package memmemov.clicon.interpreter.fs2

import cats.effect.IO
import fs2.Stream as Fs2Stream
import cats.Applicative
import memmemov.clicon.algebra.{ContributorAlgebra, StreamAlgebra}
import memmemov.clicon.interpreter.fs2.symbol.ContributorSymbol

object ContributorInterpreter:

  def apply(): ContributorAlgebra[ContributorSymbol] =
    
    new ContributorAlgebra[ContributorSymbol]:

      override type Stream = Fs2Stream[IO, Byte]

      override def createContributor(input: Stream, output: Stream): ContributorSymbol =
        ContributorSymbol(input, output)
