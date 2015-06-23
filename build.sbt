import play.sbt.PlayImport
import de.johoop.jacoco4sbt._
import de.johoop.jacoco4sbt.JacocoPlugin._

name := "sphere-sunrise"

organization := "io.sphere"

version := "1.0-SNAPSHOT"

/**
 * SUB-PROJECT DEFINITIONS
 */

lazy val commonWithTests: ClasspathDep[ProjectReference] = common % "compile;test->test;it->it"

lazy val `sphere-sunrise` = (project in file("."))
  .enablePlugins(PlayJava).configs(IntegrationTest).settings(commonSettings:_*)
  .dependsOn(commonWithTests, `product-catalog`, `setup-widget`)
  .aggregate(common, `product-catalog`, `setup-widget`, `move-to-sdk`)

lazy val common = project
  .enablePlugins(PlayJava).configs(IntegrationTest).settings(commonSettings:_*)
  .dependsOn(`move-to-sdk`)

lazy val `product-catalog` = project
  .enablePlugins(PlayJava).configs(IntegrationTest).settings(commonSettings:_*)
  .dependsOn(commonWithTests)

lazy val `setup-widget` = project
  .enablePlugins(PlayJava).configs(IntegrationTest).settings(commonSettings:_*)

lazy val `move-to-sdk` = project
  .enablePlugins(PlayJava).configs(IntegrationTest).settings(commonSettings:_*)

/**
 * COMMON SETTINGS
 */

javaUnidocSettings

lazy val sphereJvmSdkVersion = "1.0.0-M14"

lazy val commonSettings = testSettings ++ /*testCoverageSettings ++ */Seq (
  scalaVersion := "2.10.5",
  javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
  resolvers ++= Seq (
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots"),
    Resolver.bintrayRepo("commercetools", "maven")
  ),
  libraryDependencies ++= Seq (
    "io.sphere.sdk.jvm" % "sphere-models" % sphereJvmSdkVersion,
    "io.sphere.sdk.jvm" % "sphere-play-2_4-java-client_2.10" % sphereJvmSdkVersion,
    "io.sphere" % "sphere-sunrise-design" % "0.2.0",
    "org.webjars" % "webjars-play_2.10" % "2.4.0-1",
    "com.github.jknack" % "handlebars" % "2.2.1"
  ),
  dependencyOverrides ++= Set (
    "com.google.guava" % "guava" % "18.0",
    "commons-io" % "commons-io" % "2.4"
  )
)

/**
 * TEST SETTINGS
 */

lazy val testScopes = "test,it"

lazy val testSettings = Defaults.itSettings ++ Seq (
  javaSource in IntegrationTest := baseDirectory.value / "it",
  scalaSource in IntegrationTest := baseDirectory.value / "it",
  resourceDirectory in IntegrationTest := baseDirectory.value / "it/resources",
  testOptions += Tests.Argument(TestFrameworks.JUnit, "-v"),
  libraryDependencies ++= Seq (
    "org.assertj" % "assertj-core" % "3.0.0" % testScopes,
    PlayImport.component("play-test") % "it"
  ),
  dependencyOverrides ++= Set (
    "junit" % "junit" % "4.12" % testScopes
  )
)

/**
 * TEST COVERAGE SETTINGS
 */

lazy val testCoverageTask = TaskKey[Unit]("cover", "Creates the JaCoCo reports for unit and integration tests.")

lazy val testCoverageExcludes = Seq ( )// "*views*", "*Routes*", "*controllers*routes*", "*controllers*Reverse*", "*controllers*javascript*", "*controller*ref*" )

lazy val testCoverageThresholds = Thresholds(instruction = 50, method = 50, branch = 50, complexity = 35, line = 50, clazz = 50)

lazy val testCoverageSettings =  jacoco.settings ++ itJacoco.settings ++ Seq (
  parallelExecution in jacoco.Config := false,
  jacoco.excludes in jacoco.Config := testCoverageExcludes,
  jacoco.excludes in itJacoco.Config := testCoverageExcludes,
  jacoco.thresholds in jacoco.Config := testCoverageThresholds,
  jacoco.thresholds in itJacoco.Config := testCoverageThresholds,
  testCoverageTask := { (itJacoco.cover in itJacoco.Config).value },
  testCoverageTask <<= testCoverageTask.dependsOn(jacoco.check in jacoco.Config),
  libraryDependencies ++= Seq (
    "junit" % "junit-dep" % "4.11" % "it",
    "com.novocode" % "junit-interface" % "0.11" % "it"
  )
)