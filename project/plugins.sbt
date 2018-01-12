logLevel := Level.Warn

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

resolvers += Resolver.sonatypeRepo("releases")

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.5.14")

// Play enhancer - this automatically generates getters/setters for public fields
// and rewrites accessors of these fields to use the getters/setters. Remove this
// plugin if you prefer not to have this feature, or disable on a per project
// basis using disablePlugins(PlayEnhancer) in your build.sbt
addSbtPlugin("com.typesafe.sbt" % "sbt-play-enhancer" % "1.1.0")

addSbtPlugin("com.commercetools.sunrise" % "sbt-commercetools-sunrise-theme-importer" % "0.2.0")
addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.7.0")

// Javadoc generation & publishing
addSbtPlugin("io.sphere" % "git-publisher" % "0.2")
addSbtPlugin("com.eed3si9n" % "sbt-unidoc" % "0.4.0")

// Releasing
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.4")
addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.0.1")