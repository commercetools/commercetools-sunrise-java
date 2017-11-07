package com.commercetools.sunrise.framework.reverserouters.shoppingcart.cart;

import com.commercetools.sunrise.framework.reverserouters.AbstractLinksControllerComponent;
import com.commercetools.sunrise.framework.viewmodels.meta.PageMeta;

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
        meta.addHalLink(reverseRouter.cartDetailPageCall(), "cart");
        meta.addHalLink(reverseRouter.addLineItemProcessCall(), "addToCart");
        meta.addHalLink(reverseRouter.changeLineItemQuantityProcessCall(), "changeLineItem");
        meta.addHalLink(reverseRouter.removeLineItemProcessCall(), "deleteLineItem");
        meta.addHalLink(reverseRouter.addDiscountCodeProcessCall(), "addDiscountCode");
        meta.addHalLink(reverseRouter.removeDiscountCodeProcessCall(), "removeDiscountCode");
    }
}
