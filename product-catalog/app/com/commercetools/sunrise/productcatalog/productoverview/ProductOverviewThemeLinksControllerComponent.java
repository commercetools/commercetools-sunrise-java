package com.commercetools.sunrise.productcatalog.productoverview;

import com.commercetools.sunrise.common.pages.AbstractThemeLinksControllerComponent;
import com.commercetools.sunrise.common.pages.PageMeta;
import com.commercetools.sunrise.common.reverserouter.ProductReverseRouter;

import javax.inject.Inject;

final class ProductOverviewThemeLinksControllerComponent extends AbstractThemeLinksControllerComponent {

    private final ProductReverseRouter reverseRouter;

    @Inject
    ProductOverviewThemeLinksControllerComponent(final ProductReverseRouter reverseRouter) {
        this.reverseRouter = reverseRouter;
    }

    @Override
    protected void addThemeLinks(final PageMeta meta) {
        meta.addHalLink(reverseRouter.processSearchProductsForm(), "search");
    }
}