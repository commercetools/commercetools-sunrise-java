import sbt.Keys._
import sbt._
import sbtrelease.ReleasePlugin.autoImport._
import com.typesafe.sbt.pgp.PgpKeys

object Release {
  private val pathToPgpPassphrase = System.getProperty("user.home") + "/.sbt/gpg/passphrase"

  lazy val publishSettings = Seq(
    publishTo <<= version { (v: String) =>
      val nexus = "https://oss.sonatype.org/"
      if (v.trim.endsWith("SNAPSHOT"))
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases"  at nexus + "service/local/staging/deploy/maven2")
    },
    publishMavenStyle := true,
    publishArtifact in Test := false,
    releasePublishArtifactsAction := PgpKeys.publishSigned.value,
    PgpKeys.pgpPassphrase in Global := {
      val pgpPassphraseFile = file(pathToPgpPassphrase)
      if(pgpPassphraseFile.exists && pgpPassphraseFile.canRead) {
        Option(IO.read(pgpPassphraseFile).toCharArray)
      } else
        None
    },
    licenses := Seq("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
    homepage := Some(url("https://github.com/commercetools/commercetools-sunrise-java")),
    pomIncludeRepository := { _ => false },
    pomExtra := (
      <scm>
        <url>git@github.com:scommercetools/commercetools-sunrise-java.git</url>
        <connection>scm:git:git@github.com:commercetools/commercetools-sunrise-java.git</connection>
      </scm>
        <developers>
          <developer>
            <id>laura</id>
            <name>Laura Luiz</name>
            <url>https://github.com/lauraluiz</url>
          </developer>
          <developer>
            <id>michael</id>
            <name>Michael Schleichardt</name>
            <url>https://github.com/schleichardt</url>
          </developer>
        </developers>
      )
  )
}