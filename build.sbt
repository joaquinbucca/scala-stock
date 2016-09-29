enablePlugins(JavaAppPackaging)

name := """scala-products"""
organization := "com.theiterators"
version := "1.0"
scalaVersion := "2.11.8"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val akkaV       = "2.4.3"
  val circeV       = "0.5.1"
  val phantomV       = "1.22.0"
  val scalaTestV  = "2.2.6"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "com.typesafe.akka" %% "akka-stream" % akkaV,
    "com.typesafe.akka" %% "akka-http-experimental" % akkaV,
    "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaV,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaV,

    "com.websudos"        %%  "phantom-dsl"                 % phantomV,
    "com.websudos"        %%  "phantom-reactivestreams"     % phantomV,
    "com.websudos"        %%  "util-testing"                % "0.13.0"    % "test, provided",

    "de.heikoseeberger" %% "akka-http-circe" % "1.6.0",

    "io.circe" %% "circe-core" % circeV,
    "io.circe" %% "circe-generic" % circeV,
    "io.circe" %% "circe-parser" % circeV,

    "org.scalatest"     %% "scalatest" % scalaTestV % "test"
  )
}

Revolver.settings
