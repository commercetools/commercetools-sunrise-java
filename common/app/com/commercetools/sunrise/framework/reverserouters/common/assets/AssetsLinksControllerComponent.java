package com.commercetools.sunrise.framework.reverserouters.common.assets;

import com.commercetools.sunrise.framework.reverserouters.AbstractLinksControllerComponent;
import com.commercetools.sunrise.framework.viewmodels.meta.PageMeta;

import javax.inject.Inject;

public class AssetsLinksControllerComponent extends AbstractLinksControllerComponent<AssetsReverseRouter> {

    private final AssetsReverseRouter reverseRouter;

    @Inject
    protected AssetsLinksControllerComponent(final AssetsReverseRouter reverseRouter) {
        this.reverseRouter = reverseRouter;
    }

    @Override
    public final AssetsReverseRouter getReverseRouter() {
        return reverseRouter;
    }

    @Override
    protected void addLinksToPage(final PageMeta meta, final AssetsReverseRouter reverseRouter) {
        meta.setAssetsPath(reverseRouter.assetCall("").url());
    }
}