name := "Paint factory"

organization := "vitalcode"

version := "0.0.1"

scalaVersion := "2.12.8"

libraryDependencies ++= {
  val akkaV = "2.6.1"
  val akkaHttpV = "10.1.11"
  val akkaHttpCirceV = "1.30.0"
  val scalaTestV = "3.1.0"
  val circeV = "0.12.3"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-stream" % akkaV,
    "com.typesafe.akka" %% "akka-http" % akkaHttpV,
    "io.circe" %% "circe-core" % circeV,
    "io.circe" %% "circe-generic" % circeV,
    "io.circe" %% "circe-parser" % circeV,
    "de.heikoseeberger" %% "akka-http-circe" % akkaHttpCirceV,
    "com.typesafe.akka" %% "akka-testkit" % akkaV % "test",
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpV % "test",
    "org.scalatest" %% "scalatest" % scalaTestV % "test"
  )
}
