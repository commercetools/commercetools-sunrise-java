name := "sphere-sunrise"

organization := "io.sphere"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava).configs(IntegrationTest)

scalaVersion := Option(System.getProperty("scala.version")).getOrElse("2.10.4")

resolvers += Resolver.sonatypeRepo("snapshots")

libraryDependencies ++= Seq(
  "io.sphere" %% "sphere-play-sdk" % "1.0.0-demo-1-SNAPSHOT"
)