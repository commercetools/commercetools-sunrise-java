import java.text.SimpleDateFormat
import java.util.Date

import play.sbt.PlayImport

import scala.util.{Success, Try}

import ReleaseTransformations._

name := "sphere-sunrise"

organization := "io.sphere"

lazy val sunriseDesignVersion = "0.42.0"

lazy val sphereJvmSdkVersion = "1.0.0-M21"

lazy val jacksonVersion = "2.6.0"


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

lazy val commonSettings = testSettings ++ releaseSettings ++ Seq (
  scalaVersion := "2.10.6",
  javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
  resolvers ++= Seq (
    Resolver.sonatypeRepo("releases"),
    Resolver.sonatypeRepo("snapshots"),
    Resolver.bintrayRepo("commercetools", "maven"),
    Resolver.mavenLocal
  ),
  libraryDependencies ++= Seq (
    "io.sphere.sdk.jvm" % "sphere-models" % sphereJvmSdkVersion,
    "io.sphere.sdk.jvm" % "sphere-play-2_4-java-client_2.10" % sphereJvmSdkVersion,
    "io.sphere" % "sphere-sunrise-design" % sunriseDesignVersion,
    "org.webjars" % "webjars-play_2.10" % "2.4.0-1",
    "com.github.jknack" % "handlebars" % "2.2.3",
    filters,
    "commons-beanutils" % "commons-beanutils" % "1.9.2",
    "commons-io" % "commons-io" % "2.4"
  ),
  dependencyOverrides ++= Set (
    "com.google.guava" % "guava" % "18.0",
    "commons-io" % "commons-io" % "2.4",
    "commons-logging" % "commons-logging" % "1.1.3",
    "io.netty" % "netty" % "3.10.4.Final",
    "com.fasterxml.jackson.core" % "jackson-annotations" % jacksonVersion,
    "com.fasterxml.jackson.core" % "jackson-core" % jacksonVersion,
    "com.fasterxml.jackson.core" % "jackson-databind" % jacksonVersion,
    "com.fasterxml.jackson.module" % "jackson-module-parameter-names" % jacksonVersion,
    "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310" % jacksonVersion,
    "org.scala-lang" % "scala-library" % "2.10.6",
    "org.scala-lang" % "scala-reflect" % "2.10.6"
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

resourceGenerators in Compile += Def.task {
  val file = (resourceManaged in Compile).value / "internal" / "version.json"
  val date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ").format(new Date)

  val gitCommit = Try(Process("git rev-parse HEAD").lines.head) match {
    case Success(sha) => sha
    case _ => "unknown"
  }
  val buildNumber = Option(System.getenv("BUILD_NUMBER")).getOrElse("unknown")
  val contents = s"""{
                     |  "version" : "${version.value}",
                     |  "build" : {
                     |    "date" : "$date",
                     |    "number" : "$buildNumber",
                     |    "gitCommit" : "$gitCommit"
                     |  }
                     |}""".stripMargin
  IO.write(file, contents)
  Seq(file)
}.taskValue

/**
 * RELEASE SETTINGS
 */
lazy val releaseSettings = Seq(
  releaseProcess := Seq[ReleaseStep](
    checkSnapshotDependencies,
    inquireVersions,
    runTest,
    setReleaseVersion,
    commitReleaseVersion,
    tagRelease,
    setNextVersion,
    commitNextVersion,
    pushChanges
  )
)
