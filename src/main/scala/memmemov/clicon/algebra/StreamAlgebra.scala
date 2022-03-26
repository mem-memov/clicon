package memmemov.clicon.algebra

trait StreamAlgebra[S]:

  def useStream(stream: S): S
