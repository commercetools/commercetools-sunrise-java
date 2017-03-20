package com.commercetools.sunrise.common.assets

import java.util.NoSuchElementException

import controllers.{Assets, WebJarAssets}
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

abstract class AssetsControllerBase extends Controller {

  def getAssetsController: Assets

  def getWebJarAssetsController: WebJarAssets

  /**
   * Resolves resources from the public assets.
   * In particular, it tries to resolve it first from "public/" folder and, in case it was not found there, it tries in Webjars
   * @param file file to serve
   * @return the Action that serves the file
   */
  def at(file: String): Action[AnyContent] = Action.async { implicit request =>
    val pendingWebJarResult = resolveResourceInWebJar(file, request)
    resolveResourceInPublicFolder("/public", file, request)
      .filter(result => result.header.status != NOT_FOUND)
      .recoverWith {
        case _: NoSuchElementException =>
          pendingWebJarResult
      }
  }

  def resolveResourceInPublicFolder(path: String, file: String, request: Request[AnyContent]): Future[Result] = {
    getAssetsController.at(path, file).apply(request)
  }

  def resolveResourceInWebJar(file: String, request: Request[AnyContent]): Future[Result] = {
    getWebJarAssetsController.at(file).apply(request)
  }

  def notFoundAsset: Future[Result] = Future.successful(NotFound)

}
