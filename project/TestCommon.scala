import play.sbt.PlayImport
import play.sbt.PlayImport.javaWs
import sbt.Keys._
import sbt._

object TestCommon {

  lazy val PlayTest: sbt.Configuration = config("pt") extend Test

  lazy val defaultSettings: Def.SettingsDefinition = itBaseTestSettings ++ ptBaseTestSettings ++ configCommonTestSettings("test,it,pt")

  lazy val settingsWithoutPlayTest: Def.SettingsDefinition = itBaseTestSettings ++ configCommonTestSettings("test,it")

  private val itBaseTestSettings = Defaults.itSettings ++ configTestDirs(IntegrationTest, "it")

  private val ptBaseTestSettings = inConfig(PlayTest)(Defaults.testSettings) ++ configTestDirs(PlayTest, "pt") ++ configJavaWsDependency("pt")

  def configTestDirs(config: Configuration, folderName: String) = Seq(
    javaSource in config := baseDirectory.value / folderName,
    scalaSource in config := baseDirectory.value / folderName,
    resourceDirectory in config := baseDirectory.value / s"$folderName/resources"
  )

  def configCommonTestSettings(scopes: String) = Seq(
    testOptions += Tests.Argument(TestFrameworks.JUnit, "-v"),
    libraryDependencies ++= Seq (
      "org.assertj" % "assertj-core" % "3.0.0" % scopes,
      "com.commercetools.sdk.jvm.core" % "commercetools-test-lib" % "1.0.0-RC2" % scopes,
      "org.mockito" % "mockito-core" % "2.7.9",
        PlayImport.component("play-test") % scopes
    ),
    dependencyOverrides ++= Set (
      "junit" % "junit" % "4.12" % scopes
    )
  )

  def configJavaWsDependency(scopes: String) = Seq(
    libraryDependencies ++= Seq (
      javaWs % scopes
    )
  )
}