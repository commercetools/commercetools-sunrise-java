package com.commercetools.sunrise.shoppingcart.cart.addtocart;

import com.commercetools.sunrise.common.pages.AbstractThemeLinksControllerComponent;
import com.commercetools.sunrise.common.pages.PageMeta;
import com.commercetools.sunrise.common.reverserouter.CartReverseRouter;

import javax.inject.Inject;

final class AddProductToCartThemeLinksControllerComponent extends AbstractThemeLinksControllerComponent {

    private final CartReverseRouter reverseRouter;

    @Inject
    AddProductToCartThemeLinksControllerComponent(final CartReverseRouter reverseRouter) {
        this.reverseRouter = reverseRouter;
    }

    @Override
    protected void addThemeLinks(final PageMeta meta) {
        meta.addHalLink(reverseRouter.processAddProductToCartForm(), "addToCart");
    }
}
