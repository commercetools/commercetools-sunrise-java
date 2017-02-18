package com.commercetools.sunrise.common.reverserouter.common;

import com.commercetools.sunrise.common.pages.AbstractLinksControllerComponent;
import com.commercetools.sunrise.common.pages.PageMeta;

import javax.inject.Inject;

public class WebJarAssetsLinksControllerComponent extends AbstractLinksControllerComponent<WebJarAssetsReverseRouter> {

    private final WebJarAssetsReverseRouter reverseRouter;

    @Inject
    protected WebJarAssetsLinksControllerComponent(final WebJarAssetsReverseRouter reverseRouter) {
        this.reverseRouter = reverseRouter;
    }

    @Override
    public final WebJarAssetsReverseRouter getReverseRouter() {
        return reverseRouter;
    }

    @Override
    protected void addLinksToPage(final PageMeta meta, final WebJarAssetsReverseRouter reverseRouter) {
        meta.setAssetsPath(reverseRouter.webJarAssetsCall("").url());
    }
}