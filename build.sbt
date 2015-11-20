import de.johoop.jacoco4sbt.JacocoPlugin._
import de.johoop.jacoco4sbt._
import play.sbt.PlayImport

name := "sphere-sunrise"

organization := "io.sphere"

version := "1.0-SNAPSHOT"

/**
 * SUB-PROJECT DEFINITIONS
 */

lazy val commonWithTests: ClasspathDep[ProjectReference] = common % "compile;test->test;it->it;pt->pt"

lazy val `sphere-sunrise` = (project in file("."))
  .enablePlugins(PlayJava).configs(IntegrationTest, PlayTest).settings(commonSettings:_*)
  .dependsOn(commonWithTests, `product-catalog`, `setup-widget`, purchase)
  .aggregate(common, `product-catalog`, `setup-widget`, `move-to-sdk`, purchase)

lazy val common = project
  .enablePlugins(PlayJava).configs(IntegrationTest, PlayTest).settings(commonSettings:_*)
  .dependsOn(`move-to-sdk`)

lazy val `product-catalog` = project
  .enablePlugins(PlayJava).configs(IntegrationTest, PlayTest).settings(commonSettings:_*)
  .dependsOn(commonWithTests)

lazy val purchase = project
  .enablePlugins(PlayJava).configs(IntegrationTest, PlayTest).settings(commonSettings:_*)
  .dependsOn(commonWithTests)

lazy val `setup-widget` = project
  .enablePlugins(PlayJava).configs(IntegrationTest, PlayTest).settings(commonSettings:_*)

lazy val `move-to-sdk` = project
  .enablePlugins(PlayJava).configs(IntegrationTest, PlayTest).settings(commonSettings:_*)

/**
 * COMMON SETTINGS
 */

javaUnidocSettings

lazy val sphereJvmSdkVersion = "1.0.0-M21-2015-11-13-11-39-45-facets-SNAPSHOT"
lazy val jacksonVersion = "2.6.0"

lazy val commonSettings = testSettings ++ /*testCoverageSettings ++ */Seq (
  scalaVersion := "2.10.5",
  javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
  resolvers ++= Seq (
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots"),
    Resolver.bintrayRepo("commercetools", "maven"),
    Resolver.mavenLocal
  ),
  libraryDependencies ++= Seq (
    "io.sphere.sdk.jvm" % "sphere-models" % sphereJvmSdkVersion,
    "io.sphere.sdk.jvm" % "sphere-play-2_4-java-client_2.10" % "1.0.0-M20", // % sphereJvmSdkVersion,
    "io.sphere" % "sphere-sunrise-design" % "0.26.0",
    "org.webjars" % "webjars-play_2.10" % "2.4.0-1",
    "com.github.jknack" % "handlebars" % "2.2.3"
  ),
  dependencyOverrides ++= Set (
    "com.google.guava" % "guava" % "18.0",
    "commons-io" % "commons-io" % "2.4",
    "io.netty" % "netty" % "3.10.4.Final",
    "com.fasterxml.jackson.core" % "jackson-annotations" % jacksonVersion,
    "com.fasterxml.jackson.core" % "jackson-core" % jacksonVersion,
    "com.fasterxml.jackson.core" % "jackson-databind" % jacksonVersion,
    "com.fasterxml.jackson.module" % "jackson-module-parameter-names" % jacksonVersion,
    "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310" % jacksonVersion
  )
)

/**
 * TEST SETTINGS
 */
lazy val PlayTest = config("pt") extend(Test)

lazy val testScopes = "test,it,pt"

lazy val testSettings = Defaults.itSettings ++ inConfig(PlayTest)(Defaults.testSettings) ++ testDirConfigs(IntegrationTest, "it") ++ testDirConfigs(PlayTest, "pt") ++ Seq (
  testOptions += Tests.Argument(TestFrameworks.JUnit, "-v"),
  libraryDependencies ++= Seq (
    "org.assertj" % "assertj-core" % "3.0.0" % testScopes,
    PlayImport.component("play-test") % "it,pt"
  ),
  dependencyOverrides ++= Set (
    "junit" % "junit" % "4.12" % testScopes
  )
)

def testDirConfigs(config: Configuration, folderName: String) = Seq(
  javaSource in config := baseDirectory.value / folderName,
    scalaSource in config := baseDirectory.value / folderName,
    resourceDirectory in config := baseDirectory.value / s"$folderName/resources"
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
