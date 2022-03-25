package memmemov.clicon.interpreter.fs2

import memmemov.clicon.algebra.StreamAlgebra
import cats.Applicative
import cats.syntax.applicative._
import memmemov.clicon.interpreter.fs2.symbol.Stream

object StreamInterpreter:

  def apply[F[_] : Applicative](): StreamAlgebra[F, Stream] =

    new StreamAlgebra[F, Stream]:

      def useStream(stream: Stream): F[Stream] =
        summon[Applicative[F]].pure(stream)

