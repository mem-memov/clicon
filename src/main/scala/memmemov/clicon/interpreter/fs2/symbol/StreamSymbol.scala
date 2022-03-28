package memmemov.clicon.interpreter.fs2.symbol

import cats.effect.IO
import fs2.Stream as Fs2Stream
import memmemov.clicon.algebra.StreamSymbol as AlgebraStreamSymbol

case class StreamSymbol(value: Fs2Stream[IO, Byte]) extends AlgebraStreamSymbol
