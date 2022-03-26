val Http4sVersion = "1.0.0-M31"
val CirceVersion = "0.14.1"


organization := "memmemov"
name := "clicon"
version := "0.0.1"
scalaVersion := "3.1.1"
libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-ember-server" % Http4sVersion,
  "org.http4s" %% "http4s-dsl" % Http4sVersion,
  "co.fs2" %% "fs2-core" % "3.2.5"
)
