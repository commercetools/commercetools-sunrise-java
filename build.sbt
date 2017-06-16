import sbt.Keys._
import sbt._

name := "commercetools-sunrise"

organization in ThisBuild := "com.commercetools.sunrise"

scalaVersion in ThisBuild := "2.11.8"

javacOptions in Compile ++= Seq("-source", "1.8")

// see https://github.com/sbt/sbt/issues/355#issuecomment-3817629
javacOptions in (Compile, compile) ++= Seq("-target", "1.8")

resolvers in ThisBuild ++= Seq (
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots"),
  Resolver.mavenLocal
)

Release.publishSettings

Heroku.deploySettings

Version.generateVersionInfo

val childProjects: List[sbt.ProjectReference] =
  List(common, `product-catalog`, `shopping-cart`, `my-account`, wishlist, `test-lib`)

lazy val `commercetools-sunrise` = (project in file("."))
  .enablePlugins(PlayJava, JavaUnidocPlugin, SunriseThemeImporterPlugin)
  .settings(unidocProjectFilter in (JavaUnidoc, unidoc) := inProjects(childProjects: _*))
  .settings(Release.disablePublish: _*)
  .settings(Dependencies.sunriseDefaultTheme)
  .aggregate(childProjects: _*)
  .dependsOn(`product-catalog`, `shopping-cart`, `my-account`, wishlist)

lazy val common = project
  .enablePlugins(PlayJava, GenJavadocPlugin)
  .configs(IntegrationTest, TestCommon.PlayTest)
  .settings(Release.enableSignedRelease ++ TestCommon.defaultSettings: _*)
  .settings(Dependencies.jvmSdk ++ Dependencies.sunriseTheme ++ Dependencies.sunriseModules ++ Dependencies.commonLib: _*)
  .dependsOn(`test-lib` % TestCommon.allTestScopes)

lazy val `product-catalog` = project
  .enablePlugins(PlayJava, GenJavadocPlugin)
  .configs(IntegrationTest, TestCommon.PlayTest)
  .settings(Release.enableSignedRelease ++ TestCommon.defaultSettings: _*)
  .dependsOn(commonWithTests: _*)

lazy val `shopping-cart` = project
  .enablePlugins(PlayJava, GenJavadocPlugin)
  .configs(IntegrationTest, TestCommon.PlayTest)
  .settings(Release.enableSignedRelease ++ TestCommon.defaultSettings: _*)
  .dependsOn(commonWithTests: _*)

lazy val `my-account` = project
  .enablePlugins(PlayJava, GenJavadocPlugin)
  .configs(IntegrationTest, TestCommon.PlayTest)
  .settings(Release.enableSignedRelease ++ TestCommon.defaultSettings: _*)
  .dependsOn(commonWithTests: _*)

lazy val wishlist = project
  .enablePlugins(PlayJava, GenJavadocPlugin)
  .configs(IntegrationTest, TestCommon.PlayTest)
  .settings(Release.enableSignedRelease ++ TestCommon.defaultSettings: _*)
  .dependsOn(commonWithTests: _*)

lazy val `test-lib` = project
  .enablePlugins(PlayJava, GenJavadocPlugin)
  .settings(Release.enableSignedRelease ++ TestCommon.configCommonTestSettings("compile") ++ TestCommon.configPlayDependencies("compile"): _*)
  .settings(Dependencies.jvmSdk ++ Dependencies.commonLib: _*)

lazy val commonWithTests: Seq[ClasspathDep[ProjectReference]] = Seq(common, `test-lib` % TestCommon.allTestScopes)