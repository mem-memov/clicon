package memmemov.clicon.interpreter.fs2.symbol

import fs2.Stream as Fs2Stream
import cats.effect.IO

case class Stream(value: Fs2Stream[IO, Byte])
