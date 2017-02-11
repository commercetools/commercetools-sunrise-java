package com.commercetools.sunrise.shoppingcart.checkout.payment;

import com.commercetools.sunrise.common.pages.AbstractThemeLinksControllerComponent;
import com.commercetools.sunrise.common.pages.PageMeta;
import com.commercetools.sunrise.common.reverserouter.CheckoutReverseRouter;

import javax.inject.Inject;

final class CheckoutPaymentThemeLinksControllerComponent extends AbstractThemeLinksControllerComponent {

    private CheckoutReverseRouter reverseRouter;

    @Inject
    CheckoutPaymentThemeLinksControllerComponent(final CheckoutReverseRouter reverseRouter) {
        this.reverseRouter = reverseRouter;
    }

    @Override
    protected void addThemeLinks(final PageMeta meta) {
        meta.addHalLink(reverseRouter.checkoutPaymentPageCall(), "editPaymentInfo");
        meta.addHalLink(reverseRouter.checkoutPaymentProcessFormCall(), "checkoutPaymentSubmit");
    }
}
