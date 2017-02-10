package com.commercetools.sunrise.shoppingcart.cart.changelineitemquantity;

import com.commercetools.sunrise.common.pages.AbstractThemeLinksControllerComponent;
import com.commercetools.sunrise.common.pages.PageMeta;
import com.commercetools.sunrise.common.reverserouter.CartLocalizedReverseRouter;

import javax.inject.Inject;

final class ChangeLineItemQuantityThemeLinksControllerComponent extends AbstractThemeLinksControllerComponent {

    private final CartLocalizedReverseRouter reverseRouter;

    @Inject
    ChangeLineItemQuantityThemeLinksControllerComponent(final CartLocalizedReverseRouter reverseRouter) {
        this.reverseRouter = reverseRouter;
    }

    @Override
    protected void addThemeLinks(final PageMeta meta) {
        meta.addHalLink(reverseRouter.processChangeLineItemQuantityForm(), "changeLineItem");
    }
}
