package memmemov.clicon.interpreter.fs2

import memmemov.clicon.algebra.StreamAlgebra
import memmemov.clicon.interpreter.fs2.R
import cats.effect.IO
import fs2.Stream as Fs2Stream
import memmemov.clicon.interpreter.fs2.symbol.StreamSymbol

object StreamInterpreter:

  type Stream = Fs2Stream[IO, Byte]

  def apply(): StreamAlgebra[Stream] =

    new StreamAlgebra[Stream]:

      override def useStream(stream: Stream): StreamSymbol =
        StreamSymbol(stream)

