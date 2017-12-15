package com.commercetools.sunrise.core.i18n.api

import java.net.URL
import javax.inject.{Inject, Provider, Singleton}

import com.google.inject.ProvisionException
import io.sphere.sdk.projects.Project
import play.api.i18n._
import play.api.{Configuration, Environment, Logger}
import play.ext.i18n.MessagesLoader
import play.ext.i18n.MessagesLoaders.YamlFileLoader
import play.utils.Resources

@Singleton
class SunriseMessagesApi @Inject()(environment: Environment, configuration: Configuration, langs: Langs) extends DefaultMessagesApi(environment, configuration, langs) {

  protected lazy val fallbackPath: Option[String] = configuration.getString("play.i18n.fallbackPath")

  override protected def loadMessages(file: String): Map[String, String] = {

    def loadMessages(file: String, loader: MessagesLoader): Map[String, String] = {
      import scala.collection.JavaConverters._

      def loadFiles(path: Option[String]) = {
        def joinPaths(first: Option[String], second: String) = first match {
          case Some(parent) => new java.io.File(parent, second).getPath
          case None => second
        }
        environment.classLoader.getResources(joinPaths(path, file)).asScala.toList
      }

      def parseFile(messageFile: URL): Map[String, String] = loader(Messages.UrlMessageSource(messageFile), messageFile.toString).fold(e => throw e, identity)

      def fallbackFiles = if (fallbackPath.isDefined) loadFiles(fallbackPath) else List()

      (loadFiles(messagesPrefix) ++ fallbackFiles)
        .filterNot(Resources.isDirectory(environment.classLoader, _))
        .reverse
        .map(parseFile)
        .foldLeft(Map.empty[String, String]) { _ ++ _ }
    }

    super.loadMessages(file) ++ loadMessages(s"$file.yaml", YamlFileLoader)
  }

  override def translate(key: String, args: Seq[Any])(implicit lang: Lang): Option[String] = {

    def extractNamedArgs: Option[Seq[(Any, Any)]] = {
      val isNamedArgs = args.forall(arg => arg.isInstanceOf[(_, _)] && arg.asInstanceOf[(_, _)]._1.isInstanceOf[String])
      if (isNamedArgs) Some(args.map(arg => arg.asInstanceOf[(String, _)])) else None
    }

    def translate(genericKey: String, namedArgs: Seq[(Any, Any)]): Option[String] = {
      def format(pattern: String): String = namedArgs.filter(_._2 != null)
        .foldLeft(pattern)((acc, entry) => acc.replace("__" + entry._1 + "__", entry._2.toString))

      def generateSpecificKey(key: String): String = {
        def pluralizeKey(key: String): String = key + "_plural"
        def isPluralMessage = namedArgs
          .filter(tuple => tuple._1 == "count" && tuple._2.isInstanceOf[Number])
          .exists(_._2.asInstanceOf[Number].doubleValue != 1)

        if (isPluralMessage) pluralizeKey(key) else key
      }

      val codesToTry = Seq(lang.code, lang.language, "default", "default.play")
      val specificKey = generateSpecificKey(genericKey)
      codesToTry.foldLeft[Option[String]](None)((res, code) => res.orElse(messages.get(code)
        .flatMap(codeMessages => codeMessages.get(specificKey).orElse(codeMessages.get(genericKey)))))
        .map(pattern => format(pattern))
    }

    extractNamedArgs match {
      case None => super.translate(key, args)
      case Some(namedArgs) => translate(key, namedArgs)
    }
  }
}

@Singleton
class SunriseLangs @Inject()(configuration: Configuration, projectProvider: Provider[Project]) extends DefaultLangs(configuration) {
  import scala.collection.JavaConversions._

  override val availables: Seq[Lang] = configuredLangs match {
    case None | Some(Nil) => fallbackLangs
    case Some(langs) => langs.map(Lang.apply)
  }

  lazy val fallbackLangs: Seq[Lang] = loadFallbackLangs

  def configuredLangs: Option[Seq[String]] = configuration.getStringSeq("play.i18n.langs")

  def loadFallbackLangs: Seq[Lang] = {
    try {
      val projectLangs = projectProvider.get.getLanguageLocales.map(Lang.apply)
      if (projectLangs.isEmpty) Seq(Lang.defaultLang) else projectLangs
    } catch {
      case pe: ProvisionException =>
        Logger.warn("Languages from CTP could not be provided, falling back to default locale")
        Seq(Lang.defaultLang)
    }
  }
}
