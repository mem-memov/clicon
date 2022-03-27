package memmemov.clicon.interpreter.fs2

import memmemov.clicon.algebra.StreamAlgebra
import memmemov.clicon.interpreter.fs2.R

import cats.effect.IO
import fs2.Stream as Fs2Stream

object StreamInterpreter:

  def apply(): StreamAlgebra[R] =

    new StreamAlgebra[R]:

      override opaque type Stream = Fs2Stream[IO, Byte]

      override def useStream(stream: Stream): R[Stream] =
        R(stream)

