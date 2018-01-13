package com.commercetools.sunrise.controllers;

import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.common.assets.AssetsReverseRouter;
import controllers.Assets;
import controllers.WebJarAssets;
import play.api.mvc.Action;
import play.api.mvc.AnyContent;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class AssetsController extends AssetsControllerBase {

    private final Assets assetsController;
    private final WebJarAssets webJarAssetsController;

    @Inject
    AssetsController(final Assets assetsController, final WebJarAssets webJarAssetsController) {
        this.assetsController = assetsController;
        this.webJarAssetsController = webJarAssetsController;
    }

    @Override
    public final Assets getAssetsController() {
        return assetsController;
    }

    @Override
    public final WebJarAssets getWebJarAssetsController() {
        return webJarAssetsController;
    }

    @SunriseRoute(AssetsReverseRouter.ASSET)
    public Action<AnyContent> at(String file) {
        return super.at(file);
    }
}