import sbt.Keys._
import sbt._
import UnidocKeys._

name := "commercetools-sunrise"

organization in ThisBuild := "com.commercetools.sunrise"

scalaVersion in ThisBuild := "2.11.8"

javacOptions in ThisBuild ++= Seq("-source", "1.8", "-target", "1.8")

resolvers in ThisBuild ++= Seq (
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots"),
  Resolver.mavenLocal
)

Release.publishSettings

Heroku.deploySettings

Version.generateVersionInfo

val childProjects: List[sbt.ProjectReference] =
  List(common, `product-catalog`, `shopping-cart`, `my-account`, `move-to-sdk`, `sbt-tasks`)

lazy val `commercetools-sunrise` = (project in file("."))
  .enablePlugins(PlayJava)
  .settings(javadocSettings ++ Release.disablePublish: _*)
  .settings(Dependencies.sunriseDefaultTheme)
  .aggregate(childProjects: _*)
  .dependsOn(`product-catalog`, `shopping-cart`, `my-account`)

lazy val common = project
  .enablePlugins(PlayJava)
  .configs(IntegrationTest, TestCommon.PlayTest)
  .settings(Release.enableSignedRelease ++ TestCommon.defaultSettings: _*)
  .settings(Dependencies.jvmSdk ++ Dependencies.sunriseTheme ++ Dependencies.sunriseModules ++ Dependencies.commonLib: _*)
  .dependsOn(`move-to-sdk`)

lazy val `product-catalog` = project
  .enablePlugins(PlayJava)
  .configs(IntegrationTest, TestCommon.PlayTest)
  .settings(Release.enableSignedRelease ++ TestCommon.defaultSettings: _*)
  .dependsOn(commonWithTests)

lazy val `shopping-cart` = project
  .enablePlugins(PlayJava)
  .configs(IntegrationTest, TestCommon.PlayTest)
  .settings(Release.enableSignedRelease ++ TestCommon.defaultSettings: _*)
  .dependsOn(commonWithTests)

lazy val `my-account` = project
  .enablePlugins(PlayJava)
  .configs(IntegrationTest, TestCommon.PlayTest)
  .settings(Release.enableSignedRelease ++ TestCommon.defaultSettings: _*)
  .dependsOn(commonWithTests)

lazy val `sbt-tasks` = project
  .enablePlugins(PlayJava)
  .configs(IntegrationTest)
  .settings(Release.enableSignedRelease ++ TestCommon.settingsWithoutPlayTest ++ enableLibFolderInTest: _*)

lazy val `move-to-sdk` = project
  .enablePlugins(PlayJava)
  .configs(IntegrationTest)
  .settings(Release.enableSignedRelease ++ TestCommon.settingsWithoutPlayTest: _*)
  .settings(Dependencies.jvmSdk)

lazy val commonWithTests: ClasspathDep[ProjectReference] = common % "compile;test->test;it->it;pt->pt"

lazy val javadocSettings = javaUnidocSettings ++ Seq (
  unidocProjectFilter in (JavaUnidoc, unidoc) := inProjects(childProjects: _*)
)

lazy val enableLibFolderInTest = unmanagedBase in Test := baseDirectory.value / "test" / "lib"