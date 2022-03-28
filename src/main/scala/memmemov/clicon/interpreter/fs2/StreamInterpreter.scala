package memmemov.clicon.interpreter.fs2

import memmemov.clicon.algebra.StreamAlgebra
import memmemov.clicon.algebra.symbol as AS
import memmemov.clicon.interpreter.fs2.R

object StreamInterpreter:

  def apply(): StreamAlgebra[R] =

    new StreamAlgebra[R]:

      override def useStream(stream: AS.StreamSymbol): R[AS.StreamSymbol] =
        R(stream)

