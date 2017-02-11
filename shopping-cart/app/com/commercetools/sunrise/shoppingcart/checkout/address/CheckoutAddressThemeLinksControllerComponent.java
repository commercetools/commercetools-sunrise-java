package com.commercetools.sunrise.shoppingcart.checkout.address;

import com.commercetools.sunrise.common.pages.AbstractThemeLinksControllerComponent;
import com.commercetools.sunrise.common.pages.PageMeta;
import com.commercetools.sunrise.common.reverserouter.CheckoutReverseRouter;

import javax.inject.Inject;

final class CheckoutAddressThemeLinksControllerComponent extends AbstractThemeLinksControllerComponent {

    private final CheckoutReverseRouter reverseRouter;

    @Inject
    CheckoutAddressThemeLinksControllerComponent(final CheckoutReverseRouter reverseRouter) {
        this.reverseRouter = reverseRouter;
    }

    @Override
    protected void addThemeLinks(final PageMeta meta) {
        meta.addHalLink(reverseRouter.checkoutAddressesPageCall(), "checkout", "editShippingAddress", "editBillingAddress");
        meta.addHalLink(reverseRouter.checkoutAddressesProcessFormCall(), "checkoutAddressSubmit");
    }
}
