package memmemov.clicon.algebra

trait StreamAlgebra[F[_], Stream]:

  def useStream(stream: Stream): F[Stream]
