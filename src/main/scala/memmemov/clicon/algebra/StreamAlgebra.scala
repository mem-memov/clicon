package memmemov.clicon.algebra

import memmemov.clicon.algebra.symbol.StreamSymbol

trait StreamAlgebra[R[_]]:

  def useStream(stream: StreamSymbol): R[StreamSymbol]
