package com.commercetools.sunrise.common.reverserouter.shoppingcart;

import com.commercetools.sunrise.common.pages.AbstractLinksControllerComponent;
import com.commercetools.sunrise.common.pages.PageMeta;

import javax.inject.Inject;

public class CartLinksControllerComponent extends AbstractLinksControllerComponent<CartReverseRouter> {

    private final CartReverseRouter reverseRouter;

    @Inject
    protected CartLinksControllerComponent(final CartReverseRouter reverseRouter) {
        this.reverseRouter = reverseRouter;
    }

    @Override
    public final CartReverseRouter getReverseRouter() {
        return reverseRouter;
    }

    @Override
    protected void addLinksToPage(final PageMeta meta, final CartReverseRouter reverseRouter) {
        meta.addHalLink(reverseRouter.showCart(), "cart");
        meta.addHalLink(reverseRouter.processAddProductToCartForm(), "addToCart");
        meta.addHalLink(reverseRouter.processChangeLineItemQuantityForm(), "changeLineItem");
        meta.addHalLink(reverseRouter.processDeleteLineItemForm(), "deleteLineItem");
    }
}
