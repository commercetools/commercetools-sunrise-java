package com.commercetools.sunrise.shoppingcart.cart.addtocart;

import com.commercetools.sunrise.common.pages.DefaultWhateverComponent;
import com.commercetools.sunrise.common.pages.PageMeta;
import com.commercetools.sunrise.common.reverserouter.CartReverseRouter;

import javax.inject.Inject;

public class SunriseAddProductToCartHeroldComponent extends DefaultWhateverComponent {
    @Inject
    private CartReverseRouter reverseRouter;

    protected void updateMeta(final PageMeta meta) {
        meta.addHalLink(reverseRouter.processAddProductToCartForm(languageTag()), "addToCart");
    }
}
