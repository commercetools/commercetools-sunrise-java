import java.text.SimpleDateFormat
import java.util.Date

import play.sbt.PlayImport
import complete.DefaultParsers._

import sbt._
import sbt.Keys._

import scala.util.{Success, Try}

import ReleaseTransformations._

name := "commercetools-sunrise"

organization := "io.commercetools.sunrise"

lazy val sunriseDesignVersion = "0.54.0"

lazy val jvmSdkVersion = "1.0.0-RC1"

lazy val jacksonVersion = "2.6.0"


/**
 * SUB-PROJECT DEFINITIONS
 */

lazy val commonWithTests: ClasspathDep[ProjectReference] = common % "compile;test->test;it->it;pt->pt"

lazy val `commercetools-sunrise` = (project in file("."))
  .enablePlugins(PlayJava, DockerPlugin).configs(IntegrationTest, PlayTest)
  .settings(commonSettings ++ commonTestSettings ++ dockerSettings: _*)
  .dependsOn(commonWithTests, `product-catalog`, `shopping-cart`, `my-account`, `setup-widget`)
  .aggregate(common, `product-catalog`, `shopping-cart`, `my-account`, `setup-widget`, `move-to-sdk`)

lazy val common = project
  .enablePlugins(PlayJava).configs(IntegrationTest, PlayTest)
  .settings(commonSettings ++ commonTestSettings ++ jvmSdkDependencies ++ themeDependencies ++ commonDependencies ++ disableDockerPublish: _*)
  .dependsOn(`move-to-sdk`)

lazy val `product-catalog` = project
  .enablePlugins(PlayJava).configs(IntegrationTest, PlayTest)
  .settings(commonSettings ++ commonTestSettings ++ disableDockerPublish: _*)
  .dependsOn(commonWithTests)

lazy val `shopping-cart` = project
  .enablePlugins(PlayJava).configs(IntegrationTest, PlayTest)
  .settings(commonSettings ++ commonTestSettings ++ disableDockerPublish: _*)
  .dependsOn(commonWithTests)

lazy val `my-account` = project
  .enablePlugins(PlayJava).configs(IntegrationTest, PlayTest)
  .settings(commonSettings ++ commonTestSettings ++ disableDockerPublish: _*)
  .dependsOn(commonWithTests)

lazy val `setup-widget` = project
  .enablePlugins(PlayJava).configs(IntegrationTest, PlayTest)
  .settings(commonSettings ++ commonTestSettings ++ jvmSdkDependencies ++ disableDockerPublish: _*)

lazy val `move-to-sdk` = project
  .enablePlugins(PlayJava).configs(IntegrationTest)
  .settings(commonSettings ++ testSettingsWithoutPt ++ jvmSdkDependencies ++ disableDockerPublish: _*)

/**
 * COMMON SETTINGS
 */

javaUnidocSettings

lazy val commonSettings = releaseSettings ++ Seq (
  scalaVersion := "2.10.6",
  javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
  dependencyOverrides ++= Set (
    "org.slf4j" % "slf4j-api" % "1.7.15",
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

lazy val jvmSdkDependencies = Seq (
  resolvers += Resolver.sonatypeRepo("releases"),
  libraryDependencies ++= Seq (
    "com.commercetools.sdk.jvm.core" % "commercetools-models" % jvmSdkVersion,
    "com.commercetools.sdk.jvm.scala-add-ons" %% "commercetools-play-2_4-java-client" % jvmSdkVersion
  )
)

lazy val themeDependencies = Seq (
  resolvers += Resolver.bintrayRepo("commercetools", "maven"),
  libraryDependencies ++= Seq (
    "io.commercetools.sunrise" % "commercetools-sunrise-theme" % sunriseDesignVersion,
    "org.webjars" %% "webjars-play" % "2.4.0-1",
    "com.github.jknack" % "handlebars" % "2.3.2"
  )
)

lazy val commonDependencies = Seq (
  libraryDependencies ++= Seq (
    filters,
    "commons-beanutils" % "commons-beanutils" % "1.9.2",
    "commons-io" % "commons-io" % "2.4"
  )
)

lazy val dockerSettings = Seq(
  version in Docker := Option(System.getenv("BUILD_VCS_NUMBER")).getOrElse("latest"),
  packageName in Docker := "sunrise",
  dockerRepository := Some("sphereio"),
  dockerExposedPorts := Seq(9000),
  dockerCmd := Seq("-Dconfig.resource=prod.conf", "-Dlogger.resource=logback-prod.xml")
)

lazy val disableDockerPublish = Seq(
  publish in Docker := {},
  publishLocal in Docker := {}
)

/**
 * TEST SETTINGS
 */

lazy val PlayTest = config("pt") extend(Test)

lazy val commonTestSettings = itBaseTestSettings ++ ptBaseTestSettings ++ configCommonTestSettings("test,it,pt")

lazy val testSettingsWithoutPt = itBaseTestSettings ++ configCommonTestSettings("test,it")

lazy val itBaseTestSettings = Defaults.itSettings ++ configTestDirs(IntegrationTest, "it")

lazy val ptBaseTestSettings = inConfig(PlayTest)(Defaults.testSettings) ++ configTestDirs(PlayTest, "pt") ++ Seq (
  libraryDependencies ++= Seq (
    javaWs % "pt"
  )
)

def configTestDirs(config: Configuration, folderName: String) = Seq(
  javaSource in config := baseDirectory.value / folderName,
  scalaSource in config := baseDirectory.value / folderName,
  resourceDirectory in config := baseDirectory.value / s"$folderName/resources"
)

def configCommonTestSettings(scopes: String) = Seq(
  testOptions += Tests.Argument(TestFrameworks.JUnit, "-v"),
  unmanagedBase in Test := baseDirectory.value / "test" / "lib",
  libraryDependencies ++= Seq (
    "org.assertj" % "assertj-core" % "3.0.0" % scopes,
    PlayImport.component("play-test") % scopes
  ),
  dependencyOverrides ++= Set (
    "junit" % "junit" % "4.12" % scopes
  )
)

/**
 * VERSION
 */

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

/**
 * HEROKU SETTINGS
 */

stage := {
  val f = (stage in Universal).value

  val log = streams.value.log
  log.info("Cleaning submodules' target directories")

  sbt.IO.listFiles(baseDirectory.value, new FileFilter {
    override def accept(pathname: File): Boolean =
      (pathname / "target" exists()) && !pathname.getName.equals("project")
  }).foreach(f => {
    val t = f / "target"
    sbt.IO.delete(t)
    log.info(s"Removed ${t}")
  })

  f
}

/**
 * TEMPLATE SETTINGS
 */

val copyTemplateFiles = inputKey[Unit]("Copies the provided template files into the project to enable editing, e.g.: 'copyTemplateFiles common/logo.hbs cart.hbs'")

copyTemplateFiles := Def.inputTaskDyn {
  val args: Seq[String] = spaceDelimited("<arg>").parsed
  val templatePaths: Seq[String] = args.map(filePath => "templates/" + filePath)
  val confFolder: String = (resourceDirectory in Compile).value.getPath
  runMainInCompile(confFolder, templatePaths)
}.evaluated

val copyI18nFiles = inputKey[Unit]("Copies the provided i18n files into the project to enable editing, e.g.: 'copyI18nFiles en/home.yaml de/home.yaml'")

copyI18nFiles := Def.inputTaskDyn {
  val args: Seq[String] = spaceDelimited("<arg>").parsed
  val i18nPaths: Seq[String] = args.map(filePath => "i18n/" + filePath)
  val confFolder: String = (resourceDirectory in Compile).value.getPath
  runMainInCompile(confFolder, i18nPaths)
}.evaluated

def runMainInCompile(dest: String, args: Seq[String]) = Def.taskDyn {
  (runMain in Compile).toTask(s" tasks.WebjarsFilesCopier $dest ${args.mkString(" ")}")
}
