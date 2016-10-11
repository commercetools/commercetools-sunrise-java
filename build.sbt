import java.text.SimpleDateFormat
import java.util.Date

import play.sbt.PlayImport
import sbt.Keys._
import sbt._
import sbt.complete.DefaultParsers._

import scala.util.{Success, Try}

name := "commercetools-sunrise"

organization in ThisBuild := "com.commercetools.sunrise"

lazy val sunriseThemeVersion = "0.61.1"

lazy val jvmSdkVersion = "1.3.0"

lazy val jacksonVersion = "2.7.5"


/**
 * SUB-PROJECT DEFINITIONS
 */

lazy val commonWithTests: ClasspathDep[ProjectReference] = common % "compile;test->test;it->it;pt->pt"

lazy val `commercetools-sunrise` = (project in file("."))
  .enablePlugins(PlayJava, DockerPlugin).configs(IntegrationTest, PlayTest)
  .settings(commonSettings ++ commonTestSettings ++ sunriseThemeSettings ++ dockerSettings: _*)
  .dependsOn(commonWithTests, `product-catalog`, `shopping-cart`, `my-account`)
  .aggregate(common, `product-catalog`, `shopping-cart`, `my-account`, `sbt-tasks`, `move-to-sdk`)

lazy val common = project
  .enablePlugins(PlayJava).configs(IntegrationTest, PlayTest)
  .settings(commonSettings ++ commonTestSettings ++ jvmSdkDependencies ++ templateDependencies ++ sunriseModuleDependencies ++ commonDependencies ++ disableDockerPublish: _*)
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

lazy val `sbt-tasks` = project
  .enablePlugins(PlayJava).configs(IntegrationTest)
  .settings(commonSettings ++ testSettingsWithoutPt ++ sunriseThemeSettings ++ disableDockerPublish : _*)

lazy val `move-to-sdk` = project
  .enablePlugins(PlayJava).configs(IntegrationTest)
  .settings(commonSettings ++ testSettingsWithoutPt ++ jvmSdkDependencies ++ disableDockerPublish: _*)

/**
 * COMMON SETTINGS
 */

javaUnidocSettings

resolvers in ThisBuild ++= Seq (
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots"),
  Resolver.mavenLocal
)

lazy val commonSettings = Release.publishSettings ++ Seq (
  scalaVersion := "2.11.8",
  javacOptions ++= Seq("-source", "1.8", "-target", "1.8"),
  dependencyOverrides ++= Set (
    "com.fasterxml.jackson.core" % "jackson-annotations" % jacksonVersion,
    "com.fasterxml.jackson.core" % "jackson-core" % jacksonVersion,
    "com.fasterxml.jackson.core" % "jackson-databind" % jacksonVersion,
    "com.fasterxml.jackson.module" % "jackson-module-parameter-names" % jacksonVersion,
    "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310" % jacksonVersion
  )
)

lazy val sunriseModuleDependencies = Seq (
  libraryDependencies ++= Seq (
    "com.commercetools.sunrise.cms" % "cms-api" % "0.1.0"
  )
)

lazy val jvmSdkDependencies = Seq (
  libraryDependencies ++= Seq (
    "com.commercetools.sdk.jvm.core" % "commercetools-models" % jvmSdkVersion,
    "com.commercetools.sdk.jvm.core" % "commercetools-java-client" % jvmSdkVersion,
    "com.commercetools.sdk.jvm.core" % "commercetools-convenience" % jvmSdkVersion
  )
)

lazy val sunriseThemeSettings = Seq (
  unmanagedBase in Compile := (baseDirectory in ThisBuild).value / "lib",
  unmanagedBase in Test := baseDirectory.value / "test" / "lib",
  resolvers += Resolver.bintrayRepo("commercetools", "maven"),
  libraryDependencies ++= Seq (
    "com.commercetools.sunrise" % "commercetools-sunrise-theme" % sunriseThemeVersion
  )
)

lazy val templateDependencies = Seq (
  libraryDependencies ++= Seq (
    "org.webjars" %% "webjars-play" % "2.5.0-2",
    "com.github.jknack" % "handlebars" % "4.0.5"
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

publishMavenStyle in ThisBuild := true

publishArtifact in Test in ThisBuild := false

publishTo in ThisBuild <<= version { (v: String) =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

/**
 * TEST SETTINGS
 */

lazy val PlayTest = config("pt") extend Test

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
  (runMain in Compile in `sbt-tasks`).toTask(s" com.commercetools.sunrise.theme.WebjarsFilesCopier $dest ${args.mkString(" ")}")
}
