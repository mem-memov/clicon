package memmemov.clicon.algebra

import memmemov.clicon.algebra.symbol

trait Stream[R[_]]:

  def useStream(stream: symbol.Stream): R[symbol.Stream]
