import play.sbt.PlayImport
import play.sbt.PlayImport.javaWs
import sbt.Keys._
import sbt._

object TestCommon {

  lazy val PlayTest: sbt.Configuration = config("pt") extend Test

  lazy val defaultSettings: Def.SettingsDefinition = itBaseTestSettings ++ ptBaseTestSettings ++ configCommonTestSettings("test,it,pt") ++ configPlayDependencies("it,pt")

  lazy val settingsWithoutPlayTest: Def.SettingsDefinition = itBaseTestSettings ++ configCommonTestSettings("test,it") ++ configPlayDependencies("it")

  private val itBaseTestSettings = Defaults.itSettings ++ configTestDirs(IntegrationTest, "it")

  private val ptBaseTestSettings = inConfig(PlayTest)(Defaults.testSettings) ++ configTestDirs(PlayTest, "pt")

  def configTestDirs(config: Configuration, folderName: String) = Seq(
    javaSource in config := baseDirectory.value / folderName,
    scalaSource in config := baseDirectory.value / folderName,
    resourceDirectory in config := baseDirectory.value / s"$folderName/resources"
  )

  def configCommonTestSettings(scopes: String) = Seq(
    testOptions += Tests.Argument(TestFrameworks.JUnit, "-v"),
    // Tests that start Play apps do not take into account logback-test.xml, it can only be overridden by java options
    javaOptions in Test += "-Dlogger.resource=logback-test.xml",
    // Tests that do not fork the JVM ignore java options, it can only be overridden by system properties
    testOptions in Test += Tests.Setup(() =>
      if (sys.props.get("logger.resource").isEmpty)
        sys.props.put("logger.resource", "logback-test.xml")
    ),
    libraryDependencies ++= Seq (
      "org.assertj" % "assertj-core" % "3.6.2" % scopes,
      "org.mockito" % "mockito-core" % "2.7.9" % scopes
    ),
    dependencyOverrides ++= Set (
      "junit" % "junit" % "4.12" % scopes
    )
  )

  def configPlayDependencies(scopes: String) = Seq(
    libraryDependencies ++= Seq (
      javaWs % scopes,
      PlayImport.component("play-test") % scopes
    )
  )
}
