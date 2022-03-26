package memmemov.clicon.interpreter.fs2

import memmemov.clicon.algebra.StreamAlgebra
import cats.Applicative
import cats.syntax.applicative._

import cats.effect.IO
import fs2.Stream as Fs2Stream

object StreamInterpreter:

  def apply(): StreamAlgebra[Fs2Stream[IO, Byte]] =

    new StreamAlgebra[Fs2Stream[IO, Byte]]:

      override def useStream(stream: Fs2Stream[IO, Byte]): Fs2Stream[IO, Byte] =
        stream

