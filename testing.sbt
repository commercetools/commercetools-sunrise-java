import de.johoop.jacoco4sbt._
import JacocoPlugin._

Defaults.itSettings

parallelExecution in jacoco.Config := false

jacoco.settings

itJacoco.settings

//in IntelliJ IDEA you must mark manually this directory as test sources
javaSource in IntegrationTest := baseDirectory.value / "it"

libraryDependencies ++= Seq(
  "junit" % "junit-dep" % "4.11" % "it",
  "com.novocode" % "junit-interface" % "0.10" % "it",
  "org.easytesting" % "fest-assert" % "1.4" % "it",
  "com.typesafe.play" %% "play-test" % Play.autoImport.javaCore.revision % "it"
)

lazy val cover = TaskKey[Unit]("cover", "Creates the JaCoCo reports for unit and integration tests.")

cover := { (itJacoco.cover in itJacoco.Config).value }

cover <<= cover.dependsOn(jacoco.check in jacoco.Config)

val jacocoExcludes = Seq("views*", "*Routes*", "controllers*routes*", "controllers*Reverse*", "controllers*javascript*", "controller*ref*")

jacoco.excludes in jacoco.Config := jacocoExcludes

jacoco.excludes in itJacoco.Config := jacocoExcludes

lazy val thresholds = Thresholds(instruction = 75, method = 75, branch = 50, complexity = 35, line = 75, clazz = 75)

jacoco.thresholds in jacoco.Config := thresholds

jacoco.thresholds in itJacoco.Config := thresholds