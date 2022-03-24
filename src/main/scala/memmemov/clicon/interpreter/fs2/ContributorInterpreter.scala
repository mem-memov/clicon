package memmemov.clicon.interpreter.fs2

import cats.effect.IO
import memmemov.clicon.algebra.symbol.{OptionalByteStream, Contributor}
import memmemov.clicon.algebra.{ContributorAlgebra, StreamAlgebra}

object ContributorInterpreter:

  def apply()(using StreamAlgebra[OptionalByteStream]): ContributorAlgebra[Contributor] =
    
    new ContributorAlgebra[Contributor]:

      def createContributor(input: OptionalByteStream, output: OptionalByteStream): Contributor =
        Contributor(input, output)
