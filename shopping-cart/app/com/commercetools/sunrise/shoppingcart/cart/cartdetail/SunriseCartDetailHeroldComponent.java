package com.commercetools.sunrise.shoppingcart.cart.cartdetail;

import com.commercetools.sunrise.common.pages.HeroldComponentBase;
import com.commercetools.sunrise.common.pages.PageMeta;
import com.commercetools.sunrise.common.reverserouter.CartReverseRouter;

import javax.inject.Inject;

public class SunriseCartDetailHeroldComponent extends HeroldComponentBase {
    @Inject
    private CartReverseRouter reverseRouter;

    protected void updateMeta(final PageMeta meta) {
        meta.addHalLink(reverseRouter.showCart(languageTag()), "cart");
    }
}
