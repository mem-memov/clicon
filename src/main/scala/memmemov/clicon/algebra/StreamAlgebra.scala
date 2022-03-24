package memmemov.clicon.algebra

import memmemov.clicon.algebra.symbol.OptionalByteStream

trait StreamAlgebra[S]:

  def useStream(stream: OptionalByteStream): S
