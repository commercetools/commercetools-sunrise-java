package com.commercetools.sunrise.shoppingcart.cart.changelineitemquantity;

import com.commercetools.sunrise.common.pages.AbstractThemeLinksControllerComponent;
import com.commercetools.sunrise.common.pages.PageMeta;
import com.commercetools.sunrise.common.reverserouter.CartReverseRouter;

import javax.inject.Inject;

final class ChangeLineItemQuantityThemeLinksControllerComponent extends AbstractThemeLinksControllerComponent {

    private final CartReverseRouter reverseRouter;

    @Inject
    ChangeLineItemQuantityThemeLinksControllerComponent(final CartReverseRouter reverseRouter) {
        this.reverseRouter = reverseRouter;
    }

    @Override
    protected void addThemeLinks(final PageMeta meta) {
        meta.addHalLink(reverseRouter.processChangeLineItemQuantityForm(), "changeLineItem");
    }
}
