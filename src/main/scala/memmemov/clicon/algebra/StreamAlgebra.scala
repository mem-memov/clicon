package memmemov.clicon.algebra

trait StreamAlgebra[Stream]:

  def useStream(stream: Stream): StreamSymbol
