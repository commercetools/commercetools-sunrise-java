import sbt.Keys._
import sbt._
import sbtrelease.ReleasePlugin.autoImport._
import com.typesafe.sbt.pgp.PgpKeys

object Release {
  private val pathToPgpPassphrase = System.getProperty("user.home") + "/.sbt/gpg/passphrase"

  lazy val disablePublish: Def.Setting[Boolean] = publishArtifact := false

  lazy val enableSignedRelease = Seq(
    releasePublishArtifactsAction := PgpKeys.publishSigned.value,
    PgpKeys.pgpPassphrase in Global := {
      val pgpPassphraseFile = file(pathToPgpPassphrase)
      if(pgpPassphraseFile.exists && pgpPassphraseFile.canRead)
        Option(IO.read(pgpPassphraseFile).toCharArray)
      else None
    }
  )

  lazy val publishSettings = Seq(
    publishTo in ThisBuild := version { (v: String) =>
      val nexus = "https://oss.sonatype.org/"
      if (v.trim.endsWith("SNAPSHOT"))
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases"  at nexus + "service/local/staging/deploy/maven2")
    }.value,
    publishMavenStyle in ThisBuild := true,
    publishArtifact in Test in ThisBuild := false,
    licenses in ThisBuild := Seq("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
    homepage in ThisBuild := Some(url("https://github.com/commercetools/commercetools-sunrise-java")),
    pomIncludeRepository in ThisBuild := { _ => false },
    pomExtra in ThisBuild := (
      <scm>
        <url>git@github.com:scommercetools/commercetools-sunrise-java.git</url>
        <connection>scm:git:git@github.com:commercetools/commercetools-sunrise-java.git</connection>
      </scm>
      <developers>
        <developer>
          <id>matthiaskoester</id>
          <name>Matthias Köster</name>
          <url>https://github.com/katmatt</url>
        </developer>
        <developer>
          <id>lauraluiz</id>
          <name>Laura Luiz</name>
          <url>https://github.com/lauraluiz</url>
        </developer>
        <developer>
          <id>michaelschleichardt</id>
          <name>Michael Schleichardt</name>
          <url>https://github.com/schleichardt</url>
        </developer>
      </developers>
      )
  )
}