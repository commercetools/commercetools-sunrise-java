import play.sbt.PlayImport.{cache, filters}
import sbt.Keys._
import sbt._

object Dependencies {
  private val sunriseThemeVersion = "0.68.0"
  val sunriseEmailVersion = "0.1.2"
  private val jvmSdkVersion = "1.17.0"
  private val jacksonVersion = "2.7.5"

  lazy val jvmSdk = Seq (
    libraryDependencies ++= Seq (
      "com.commercetools.sdk.jvm.core" % "commercetools-models" % jvmSdkVersion,
      "com.commercetools.sdk.jvm.core" % "commercetools-java-client" % jvmSdkVersion,
      "com.commercetools.sdk.jvm.core" % "commercetools-convenience" % jvmSdkVersion
    ),
    dependencyOverrides ++= Set (
      "com.fasterxml.jackson.core" % "jackson-annotations" % jacksonVersion,
      "com.fasterxml.jackson.core" % "jackson-core" % jacksonVersion,
      "com.fasterxml.jackson.core" % "jackson-databind" % jacksonVersion,
      "com.fasterxml.jackson.module" % "jackson-module-parameter-names" % jacksonVersion,
      "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310" % jacksonVersion
    )
  )

  lazy val sunriseTheme = Seq (
    libraryDependencies ++= Seq (
      "org.webjars" %% "webjars-play" % "2.5.0-2",
      "com.github.jknack" % "handlebars" % "4.0.5"
    )
  )

  lazy val sunriseModules = Seq (
    libraryDependencies ++= Seq (
      "com.commercetools.sunrise.cms" % "cms-api" % "0.1.0",
      "com.commercetools.sunrise.email" % "email-api" % sunriseEmailVersion
    )
  )

  lazy val sunriseEmailSmtp = Seq {
    libraryDependencies ++= Seq (
      "com.commercetools.sunrise.email" % "email-smtp" % sunriseEmailVersion
    )
  }

  lazy val commonLib = Seq (
    libraryDependencies ++= Seq (
      filters,
      cache,
      "commons-beanutils" % "commons-beanutils" % "1.9.2",
      "commons-io" % "commons-io" % "2.4"
    )
  )

  lazy val sunriseDefaultTheme = Seq (
    resolvers += Resolver.bintrayRepo("commercetools", "maven"),
    libraryDependencies ++= Seq (
      "com.commercetools.sunrise" % "commercetools-sunrise-theme" % sunriseThemeVersion
    )
  )
}
