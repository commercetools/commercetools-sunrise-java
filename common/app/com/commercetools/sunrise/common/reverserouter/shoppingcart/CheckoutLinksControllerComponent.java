package com.commercetools.sunrise.common.reverserouter.shoppingcart;

import com.commercetools.sunrise.common.pages.AbstractLinksControllerComponent;
import com.commercetools.sunrise.common.pages.PageMeta;

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
        meta.addHalLink(reverseRouter.checkoutAddressesPageCall(), "checkout", "editShippingAddress", "editBillingAddress");
        meta.addHalLink(reverseRouter.checkoutAddressesProcessFormCall(), "checkoutAddressSubmit");
        meta.addHalLink(reverseRouter.checkoutConfirmationProcessFormCall(), "checkoutConfirmationSubmit");
        meta.addHalLink(reverseRouter.checkoutPaymentPageCall(), "editPaymentInfo");
        meta.addHalLink(reverseRouter.checkoutPaymentProcessFormCall(), "checkoutPaymentSubmit");
        meta.addHalLink(reverseRouter.checkoutShippingPageCall(), "editShippingMethod");
        meta.addHalLink(reverseRouter.checkoutShippingProcessFormCall(), "checkoutShippingSubmit");
    }
}
