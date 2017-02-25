import sbt.Keys._
import sbt._
import sbt.complete.DefaultParsers.spaceDelimited

object ThemeImporterPlugin extends AutoPlugin {

  object autoImport {

    val copyTemplateFiles: InputKey[Unit] =
      inputKey[Unit]("Copies the provided template files into the project to enable editing, e.g.: 'copyTemplateFiles common/logo.hbs cart.hbs'")

    val copyI18nFiles: InputKey[Unit] =
      inputKey[Unit]("Copies the provided i18n files into the project to enable editing, e.g.: 'copyI18nFiles en/home.yaml de/home.yaml'")

  }

  import autoImport._

  copyTemplateFiles := Def.inputTaskDyn {
    val args: Seq[String] = spaceDelimited("<arg>").parsed
    val templatePaths: Seq[String] = args.map(filePath => "templates/" + filePath)
    val confFolder: String = (resourceDirectory in Compile).value.getPath
    runMainInCompile(confFolder, templatePaths)
  }.evaluated

  copyI18nFiles := Def.inputTaskDyn {
    val args: Seq[String] = spaceDelimited("<arg>").parsed
    val i18nPaths: Seq[String] = args.map(filePath => "i18n/" + filePath)
    val confFolder: String = (resourceDirectory in Compile).value.getPath
    runMainInCompile(confFolder, i18nPaths)
  }.evaluated

  def runMainInCompile(dest: String, args: Seq[String]): Def.Initialize[Task[Unit]] = Def.taskDyn {
    (runMain in Compile).toTask(s" com.commercetools.sunrise.theme.WebjarsFilesCopier $dest ${args.mkString(" ")}")
  }
}