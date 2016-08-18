package com.commercetools.sunrise.shoppingcart.checkout.payment;

import com.commercetools.sunrise.common.pages.HeroldComponentBase;
import com.commercetools.sunrise.common.pages.PageMeta;
import com.commercetools.sunrise.common.reverserouter.CheckoutReverseRouter;

import javax.inject.Inject;

final class SunriseCheckoutPaymentHeroldComponent extends HeroldComponentBase {
    @Inject
    private CheckoutReverseRouter reverseRouter;

    protected void updateMeta(final PageMeta meta) {
        meta.addHalLink(reverseRouter.checkoutPaymentPageCall(languageTag()), "editPaymentInfo");
        meta.addHalLink(reverseRouter.checkoutPaymentProcessFormCall(languageTag()), "checkoutPaymentSubmit");
    }
}
