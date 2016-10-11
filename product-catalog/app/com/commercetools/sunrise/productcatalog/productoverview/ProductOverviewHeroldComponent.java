package com.commercetools.sunrise.productcatalog.productoverview;

import com.commercetools.sunrise.common.pages.HeroldComponentBase;
import com.commercetools.sunrise.common.pages.PageMeta;
import com.commercetools.sunrise.common.reverserouter.ProductReverseRouter;

import javax.inject.Inject;

final class ProductOverviewHeroldComponent extends HeroldComponentBase {
    @Inject
    private ProductReverseRouter reverseRouter;

    protected void updateMeta(final PageMeta meta) {
        meta.addHalLink(reverseRouter.processSearchProductsForm(languageTag()), "search");
    }
}