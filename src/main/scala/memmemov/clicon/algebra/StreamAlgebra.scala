package memmemov.clicon.algebra

trait StreamAlgebra[R[_]]:

  type Stream

  def useStream(stream: Stream): R[Stream]
