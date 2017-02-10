package com.commercetools.sunrise.shoppingcart.checkout.shipping;

import com.commercetools.sunrise.common.pages.AbstractThemeLinksControllerComponent;
import com.commercetools.sunrise.common.pages.PageMeta;
import com.commercetools.sunrise.common.reverserouter.CheckoutLocalizedReverseRouter;

import javax.inject.Inject;

final class CheckoutShippingThemeLinksControllerComponent extends AbstractThemeLinksControllerComponent {

    private final CheckoutLocalizedReverseRouter reverseRouter;

    @Inject
    CheckoutShippingThemeLinksControllerComponent(final CheckoutLocalizedReverseRouter reverseRouter) {
        this.reverseRouter = reverseRouter;
    }

    @Override
    protected void addThemeLinks(final PageMeta meta) {
        meta.addHalLink(reverseRouter.checkoutShippingPageCall(), "editShippingMethod");
        meta.addHalLink(reverseRouter.checkoutShippingProcessFormCall(), "checkoutShippingSubmit");
    }
}
