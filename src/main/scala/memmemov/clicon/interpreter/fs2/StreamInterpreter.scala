package memmemov.clicon.interpreter.fs2

import memmemov.clicon.algebra.StreamAlgebra
import memmemov.clicon.algebra.symbol.OptionalByteStream

object StreamInterpreter:

  def apply(): StreamAlgebra[OptionalByteStream] = 

    new StreamAlgebra[OptionalByteStream]:

      def useStream(stream: OptionalByteStream): OptionalByteStream =
        stream

