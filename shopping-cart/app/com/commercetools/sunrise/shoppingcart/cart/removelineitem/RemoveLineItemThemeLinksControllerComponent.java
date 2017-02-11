package com.commercetools.sunrise.shoppingcart.cart.removelineitem;

import com.commercetools.sunrise.common.pages.AbstractThemeLinksControllerComponent;
import com.commercetools.sunrise.common.pages.PageMeta;
import com.commercetools.sunrise.common.reverserouter.CartReverseRouter;

import javax.inject.Inject;

final class RemoveLineItemThemeLinksControllerComponent extends AbstractThemeLinksControllerComponent {

    private final CartReverseRouter reverseRouter;

    @Inject
    RemoveLineItemThemeLinksControllerComponent(final CartReverseRouter reverseRouter) {
        this.reverseRouter = reverseRouter;
    }

    @Override
    protected void addThemeLinks(final PageMeta meta) {
        meta.addHalLink(reverseRouter.processDeleteLineItemForm(), "deleteLineItem");
    }
}
