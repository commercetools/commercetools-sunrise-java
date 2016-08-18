package com.commercetools.sunrise.shoppingcart.cart.changelineitemquantity;

import com.commercetools.sunrise.common.pages.HeroldComponentBase;
import com.commercetools.sunrise.common.pages.PageMeta;
import com.commercetools.sunrise.common.reverserouter.CartReverseRouter;

import javax.inject.Inject;

final class SunriseChangeLineItemQuantityHeroldComponent extends HeroldComponentBase {
    @Inject
    private CartReverseRouter reverseRouter;

    protected void updateMeta(final PageMeta meta) {
        meta.addHalLink(reverseRouter.processChangeLineItemQuantityForm(languageTag()), "changeLineItem");
    }
}
