package com.commercetools.sunrise.framework.reverserouters.shoppingcart.checkout;

import com.commercetools.sunrise.framework.reverserouters.AbstractLinksControllerComponent;
import com.commercetools.sunrise.framework.viewmodels.meta.PageMeta;

import javax.inject.Inject;

public class CheckoutLinksControllerComponent extends AbstractLinksControllerComponent<CheckoutReverseRouter> {

    private final CheckoutReverseRouter reverseRouter;

    @Inject
    protected CheckoutLinksControllerComponent(final CheckoutReverseRouter reverseRouter) {
        this.reverseRouter = reverseRouter;
    }

    @Override
    public final CheckoutReverseRouter getReverseRouter() {
        return reverseRouter;
    }

    @Override
    protected void addLinksToPage(final PageMeta meta, final CheckoutReverseRouter reverseRouter) {
        meta.addHalLink(reverseRouter.checkoutAddressPageCall(), "checkout", "editShippingAddress", "editBillingAddress");
        meta.addHalLink(reverseRouter.checkoutAddressProcessCall(), "checkoutAddressSubmit");
        meta.addHalLink(reverseRouter.checkoutConfirmationProcessCall(), "checkoutConfirmationSubmit");
        meta.addHalLink(reverseRouter.checkoutPaymentPageCall(), "editPaymentInfo");
        meta.addHalLink(reverseRouter.checkoutPaymentProcessCall(), "checkoutPaymentSubmit");
        meta.addHalLink(reverseRouter.checkoutShippingPageCall(), "editShippingMethod");
        meta.addHalLink(reverseRouter.checkoutShippingProcessCall(), "checkoutShippingSubmit");
    }
}
