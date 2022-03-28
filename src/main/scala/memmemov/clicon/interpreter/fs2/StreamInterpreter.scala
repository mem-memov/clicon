package memmemov.clicon.interpreter.fs2

import memmemov.clicon.algebra
import memmemov.clicon.algebra.symbol
import memmemov.clicon.interpreter.fs2.R

object StreamInterpreter:

  def apply(): algebra.Stream[R] =

    new algebra.Stream[R]:

      override def useStream(stream: symbol.Stream): R[symbol.Stream] =
        R(stream)

