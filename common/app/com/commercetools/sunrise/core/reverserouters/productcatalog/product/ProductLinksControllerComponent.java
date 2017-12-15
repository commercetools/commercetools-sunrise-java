package com.commercetools.sunrise.core.reverserouters.productcatalog.product;

import com.commercetools.sunrise.core.reverserouters.AbstractLinksControllerComponent;
import com.commercetools.sunrise.core.viewmodels.meta.PageMeta;

import javax.inject.Inject;

public class ProductLinksControllerComponent extends AbstractLinksControllerComponent<ProductReverseRouter> {

    private final ProductReverseRouter reverseRouter;

    @Inject
    protected ProductLinksControllerComponent(final ProductReverseRouter reverseRouter) {
        this.reverseRouter = reverseRouter;
    }

    @Override
    public final ProductReverseRouter getReverseRouter() {
        return reverseRouter;
    }

    @Override
    protected void addLinksToPage(final PageMeta meta, final ProductReverseRouter reverseRouter) {
        meta.addHalLink(reverseRouter.searchProcessCall(), "search");
    }
}