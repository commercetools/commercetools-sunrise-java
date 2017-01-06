import com.typesafe.sbt.packager.universal.UniversalPlugin.autoImport._
import sbt.Keys._
import sbt._

object Heroku {

  lazy val deploySettings = Seq(
    stage := {
      val f = (stage in Universal).value

      val log = streams.value.log
      log.info("Cleaning submodules' target directories")

      sbt.IO
        .listFiles(baseDirectory.value, onlySubProjectRootFolder)
        .foreach(subProjectFolder => deleteTargetFolderFromProject(subProjectFolder))

      def onlySubProjectRootFolder: FileFilter = {
        new FileFilter {
          override def accept(pathname: File): Boolean =
            (pathname / "target" exists()) && !pathname.getName.equals("project")
        }
      }

      def deleteTargetFolderFromProject(f: File) = {
        val t = f / "target"
        sbt.IO.delete(t)
        log.info(s"Removed $t")
      }

      f
    }
  )
}