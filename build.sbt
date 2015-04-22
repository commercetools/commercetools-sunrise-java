name := "sphere-sunrise"

organization := "io.sphere"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava).configs(IntegrationTest)

scalaVersion := "2.11.6"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

resolvers += Resolver.sonatypeRepo("snapshots")

resolvers += Resolver.sonatypeRepo("releases")

val jvmSdkVersion = "1.0.0-M13"

libraryDependencies ++=
  ("io.sphere.sdk.jvm" % "sphere-models" % jvmSdkVersion withSources()) ::
  "io.sphere.sdk.jvm" %% "sphere-play-2_4-java-client" % jvmSdkVersion ::
  "com.google.inject" % "guice" % "3.0" ::
  "org.easytesting" % "fest-assert" % "1.4" % "test" ::
  Nil

dependencyOverrides += "com.typesafe.play" %% "play-omnidoc" % "2.4.0-M3"
dependencyOverrides += "com.typesafe.netty" % "netty-http-pipelining" % "1.1.4"

javaUnidocSettings