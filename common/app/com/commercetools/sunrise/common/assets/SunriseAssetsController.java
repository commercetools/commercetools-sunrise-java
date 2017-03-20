package com.commercetools.sunrise.common.assets;

import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.reverserouters.common.assets.AssetsReverseRouter;
import controllers.Assets;
import controllers.WebJarAssets;
import play.api.mvc.Action;
import play.api.mvc.AnyContent;

public abstract class SunriseAssetsController extends AssetsControllerBase {

    private final Assets assetsController;
    private final WebJarAssets webJarAssetsController;

    protected SunriseAssetsController(final Assets assetsController, final WebJarAssets webJarAssetsController) {
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
