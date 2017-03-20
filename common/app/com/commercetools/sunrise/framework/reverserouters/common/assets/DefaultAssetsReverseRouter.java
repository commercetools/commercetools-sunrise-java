package com.commercetools.sunrise.framework.reverserouters.common.assets;

import com.commercetools.sunrise.framework.reverserouters.AbstractReflectionReverseRouter;
import com.commercetools.sunrise.framework.reverserouters.ParsedRoutes;
import com.commercetools.sunrise.framework.reverserouters.ReverseCaller;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DefaultAssetsReverseRouter extends AbstractReflectionReverseRouter implements AssetsReverseRouter {

    private final ReverseCaller reverseCaller;

    @Inject
    protected DefaultAssetsReverseRouter(final ParsedRoutes parsedRoutes) {
        this.reverseCaller = getReverseCallerForSunriseRoute(ASSET, parsedRoutes);
    }

    @Override
    public Call assetCall(final String filepath) {
        return reverseCaller.call(filepath);
    }
}
