// Comment to get more information during initialization
logLevel := Level.Warn

// The Typesafe repository
resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

//temporary resolver until jacoco4sbt is compatible to Java 8
resolvers += "Schleichardts GitHub" at "http://schleichardt.github.io/jvmrepo/"

// Use the Play sbt plugin for Play projects
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.3.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "0.7.0-M3")

addSbtPlugin("io.sphere.de.johoop" % "jacoco4sbt" % "2.1.5-fork-1.0.0")