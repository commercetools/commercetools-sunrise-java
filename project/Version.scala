import java.text.SimpleDateFormat
import java.util.Date

import sbt.Keys._
import sbt._

import scala.util.{Success, Try}

object Version {

  lazy val generateVersionInfo = Seq(
    resourceGenerators in Compile += Def.task {
      val file = (resourceManaged in Compile).value / "internal" / "version.json"
      val date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ").format(new Date)

      val gitCommit = Try(Process("git rev-parse HEAD").lines.head) match {
        case Success(sha) => sha
        case _ => "unknown"
      }
      val buildNumber = Option(System.getenv("BUILD_NUMBER")).getOrElse("unknown")
      val contents = s"""{
                        |  "version" : "${version.value}",
                        |  "build" : {
                        |    "date" : "$date",
                        |    "number" : "$buildNumber",
                        |    "gitCommit" : "$gitCommit"
                        |  }
                        |}""".stripMargin
      IO.write(file, contents)
      Seq(file)
    }.taskValue
  )
}