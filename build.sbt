name := "sphere-sunrise"

organization := "io.sphere"

version := "1.0-SNAPSHOT"

scalaVersion := "2.10.3"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

resolvers in ThisBuild += Resolver.sonatypeRepo("snapshots")

resolvers in ThisBuild += Resolver.sonatypeRepo("releases")

resolvers in ThisBuild += Resolver.mavenLocal

val jvmSdkVersion = "1.0.0-M13"

libraryDependencies in ThisBuild ++=
  ("io.sphere.sdk.jvm" % "sphere-models" % jvmSdkVersion withSources()) ::
  "io.sphere.sdk.jvm" %% "sphere-play-2_4-java-client" % jvmSdkVersion ::
  "com.google.inject" % "guice" % "3.0" ::
  "com.typesafe" % "config" % "1.3.0-M3" ::
  "com.github.jknack" % "handlebars" % "2.0.0" ::
  "com.github.jknack" % "handlebars-jackson2" % "2.0.0" ::
  "io.sphere" % "sphere-sunrise-design" % "1.0.0" ::
  "org.easytesting" % "fest-assert" % "1.4" % "test,it" ::
  play.sbt.PlayImport.component("play-test") % "it" ::
  Nil

dependencyOverrides += "com.typesafe.play" %% "play-omnidoc" % "2.4.0-M3"

dependencyOverrides += "com.typesafe.netty" % "netty-http-pipelining" % "1.1.4"

javaUnidocSettings

lazy val commonWithTest: sbt.ClasspathDep[sbt.ProjectReference] = common % "compile;test->test;it->it"

lazy val root = (project in file("."))
  .configs(IntegrationTest)
  .settings(Defaults.itSettings:_*)
  .settings(javaSource in IntegrationTest := baseDirectory.value / "it")
  .enablePlugins(PlayJava)
  .dependsOn(commonWithTest, `product-catalog`)
  .aggregate(webapp, common, `product-catalog`)

lazy val common = project
  .configs(IntegrationTest)
  .settings(Defaults.itSettings:_*)
  .settings(javaSource in IntegrationTest := baseDirectory.value / "it")
  .enablePlugins(PlayJava)

lazy val `product-catalog` = project
  .configs(IntegrationTest)
  .settings(Defaults.itSettings:_*)
  .settings(javaSource in IntegrationTest := baseDirectory.value / "it")
  .enablePlugins(PlayJava)
  .dependsOn(commonWithTest)

lazy val webapp = project
  .configs(IntegrationTest)
  .settings(Defaults.itSettings:_*)
  .settings(javaSource in IntegrationTest := baseDirectory.value / "it")
  .enablePlugins(PlayJava)
  .dependsOn(commonWithTest, `product-catalog`)
