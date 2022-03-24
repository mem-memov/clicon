package memmemov.clicon.algebra.symbol

import fs2.Stream as Fs2Stream
import cats.effect.IO

type OptionalByteStream = Option[Fs2Stream[IO, Byte]]
