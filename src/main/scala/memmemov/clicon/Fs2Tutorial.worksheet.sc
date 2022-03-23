import cats.effect.unsafe.IORuntime
println("Hello, world!")
   
val x = 1
x + x

import cats.effect.IO
import cats.implicits._
import fs2.Stream
import cats.effect.unsafe.IORuntime

Stream.emit(42)
Stream(1,2,3)

implicit val runtime: IORuntime = cats.effect.unsafe.IORuntime.global

Stream.eval(IO(println("Hello"))).compile.drain.unsafeRunSync()

Stream.constant("hello").take(20).toList

Stream.iterate(0)(_ + 1).take(10).toList

val a = Stream.constant(10).take(3)
val b = Stream.iterate(0)(_ + 1).take(4)

(a ++ b).toList.map (_ - 2).toList

(a ++ b).evalMap(e => IO(println(e)).map(_ => e + 1))
