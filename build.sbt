name := "sphere-sunrise"

organization := "io.sphere"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava).configs(IntegrationTest)

scalaVersion := "2.10.4"

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

resolvers += Resolver.sonatypeRepo("snapshots")

resolvers += Resolver.sonatypeRepo("releases")

libraryDependencies ++= Seq(
  "io.sphere.sdk.jvm" % "models" % "1.0.0-M7",
  "io.sphere.sdk.jvm" % "play-2_3-java-client_2.10" % "1.0.0-M7",
  "com.google.inject" % "guice" % "3.0"
)

initialCommands in console := "import tutorial.ClientFactory.createClient;" +
  "import io.sphere.sdk.categories._;" +
  """println("Use 'val client = createClient()' to create a client instance.")"""

javaUnidocSettings