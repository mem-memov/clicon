package memmemov.clicon.interpreter.fs2.symbol

import cats.effect.IO
import fs2.Stream as Fs2Stream
import memmemov.clicon.algebra.StreamAlgebra

case class ContributorSymbol(input: Fs2Stream[IO, Byte], output: Fs2Stream[IO, Byte])
