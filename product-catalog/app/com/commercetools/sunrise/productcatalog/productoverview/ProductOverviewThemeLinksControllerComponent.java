package com.commercetools.sunrise.productcatalog.productoverview;

import com.commercetools.sunrise.common.pages.AbstractThemeLinksControllerComponent;
import com.commercetools.sunrise.common.pages.PageMeta;
import com.commercetools.sunrise.common.reverserouter.ProductLocalizedReverseRouter;

import javax.inject.Inject;

final class ProductOverviewThemeLinksControllerComponent extends AbstractThemeLinksControllerComponent {

    private final ProductLocalizedReverseRouter reverseRouter;

    @Inject
    ProductOverviewThemeLinksControllerComponent(final ProductLocalizedReverseRouter reverseRouter) {
        this.reverseRouter = reverseRouter;
    }

    @Override
    protected void addThemeLinks(final PageMeta meta) {
        meta.addHalLink(reverseRouter.processSearchProductsForm(), "search");
    }
}