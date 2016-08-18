package com.commercetools.sunrise.shoppingcart.checkout.shipping;

import com.commercetools.sunrise.common.pages.HeroldComponentBase;
import com.commercetools.sunrise.common.pages.PageMeta;
import com.commercetools.sunrise.common.reverserouter.CheckoutReverseRouter;

import javax.inject.Inject;

final class SunriseCheckoutShippingHeroldComponent extends HeroldComponentBase {
    @Inject
    private CheckoutReverseRouter reverseRouter;

    protected void updateMeta(final PageMeta meta) {
        meta.addHalLink(reverseRouter.checkoutShippingPageCall(languageTag()), "editShippingMethod");
        meta.addHalLink(reverseRouter.checkoutShippingProcessFormCall(languageTag()), "checkoutShippingSubmit");
    }
}
