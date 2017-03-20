package controllers.common;

import com.commercetools.sunrise.common.assets.SunriseAssetsController;
import controllers.Assets;
import controllers.WebJarAssets;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Controller that tries to resolve an asset from the project's "public/" folder and -if not found- tries to resolve it from the webjars
 */
@Singleton
public final class AssetsController extends SunriseAssetsController {

    @Inject
    public AssetsController(final Assets assetsController, final WebJarAssets webJarAssetsController) {
        super(assetsController, webJarAssetsController);
    }
}
