name := "sphere-sunrise"

organization := "io.sphere"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava).configs(IntegrationTest)

scalaVersion := Option(System.getProperty("scala.version")).getOrElse("2.10.4")