package com.commercetools.sunrise.shoppingcart.cart.removelineitem;

import com.commercetools.sunrise.common.pages.AbstractThemeLinksControllerComponent;
import com.commercetools.sunrise.common.pages.PageMeta;
import com.commercetools.sunrise.common.reverserouter.CartLocalizedReverseRouter;

import javax.inject.Inject;

final class RemoveLineItemThemeLinksControllerComponent extends AbstractThemeLinksControllerComponent {

    private final CartLocalizedReverseRouter reverseRouter;

    @Inject
    RemoveLineItemThemeLinksControllerComponent(final CartLocalizedReverseRouter reverseRouter) {
        this.reverseRouter = reverseRouter;
    }

    @Override
    protected void addThemeLinks(final PageMeta meta) {
        meta.addHalLink(reverseRouter.processDeleteLineItemForm(), "deleteLineItem");
    }
}
