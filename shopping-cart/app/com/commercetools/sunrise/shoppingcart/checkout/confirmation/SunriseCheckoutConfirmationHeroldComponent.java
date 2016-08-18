package com.commercetools.sunrise.shoppingcart.checkout.confirmation;

import com.commercetools.sunrise.common.pages.HeroldComponentBase;
import com.commercetools.sunrise.common.pages.PageMeta;
import com.commercetools.sunrise.common.reverserouter.CheckoutReverseRouter;

import javax.inject.Inject;

final class SunriseCheckoutConfirmationHeroldComponent extends HeroldComponentBase {
    @Inject
    private CheckoutReverseRouter checkoutReverseRouter;

    protected void updateMeta(final PageMeta meta) {
        meta.addHalLink(checkoutReverseRouter.checkoutConfirmationProcessFormCall(languageTag()), "checkoutConfirmationSubmit");
    }
}

