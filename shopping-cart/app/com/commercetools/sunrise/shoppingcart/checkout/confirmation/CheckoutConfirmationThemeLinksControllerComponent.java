package com.commercetools.sunrise.shoppingcart.checkout.confirmation;

import com.commercetools.sunrise.common.pages.AbstractThemeLinksControllerComponent;
import com.commercetools.sunrise.common.pages.PageMeta;
import com.commercetools.sunrise.common.reverserouter.CheckoutLocalizedReverseRouter;

import javax.inject.Inject;

final class CheckoutConfirmationThemeLinksControllerComponent extends AbstractThemeLinksControllerComponent {

    private final CheckoutLocalizedReverseRouter checkoutReverseRouter;

    @Inject
    CheckoutConfirmationThemeLinksControllerComponent(final CheckoutLocalizedReverseRouter checkoutReverseRouter) {
        this.checkoutReverseRouter = checkoutReverseRouter;
    }

    @Override
    protected void addThemeLinks(final PageMeta meta) {
        meta.addHalLink(checkoutReverseRouter.checkoutConfirmationProcessFormCall(), "checkoutConfirmationSubmit");
    }
}
