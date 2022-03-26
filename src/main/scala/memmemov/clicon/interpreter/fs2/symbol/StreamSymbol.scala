package memmemov.clicon.interpreter.fs2.symbol

import fs2.Stream as Fs2Stream
import cats.effect.IO

case class StreamSymbol(value: Fs2Stream[IO, Byte])
