package com.commercetools.sunrise.framework.reverserouters.productcatalog;

import com.commercetools.sunrise.common.pages.AbstractLinksControllerComponent;
import com.commercetools.sunrise.common.pages.PageMeta;

import javax.inject.Inject;

public class HomeLinksControllerComponent extends AbstractLinksControllerComponent<HomeReverseRouter> {

    private final HomeReverseRouter reverseRouter;

    @Inject
    protected HomeLinksControllerComponent(final HomeReverseRouter reverseRouter) {
        this.reverseRouter = reverseRouter;
    }

    @Override
    public final HomeReverseRouter getReverseRouter() {
        return reverseRouter;
    }

    @Override
    protected void addLinksToPage(final PageMeta meta, final HomeReverseRouter reverseRouter) {
        meta.addHalLink(reverseRouter.homePageCall(), "home", "continueShopping");
    }
}