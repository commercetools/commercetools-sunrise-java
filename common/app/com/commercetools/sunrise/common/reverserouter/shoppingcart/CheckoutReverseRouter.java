package com.commercetools.sunrise.common.reverserouter.shoppingcart;

import com.commercetools.sunrise.common.reverserouter.LocalizedReverseRouter;
import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(ReflectionCheckoutLocalizedReverseRouter.class)
public interface CheckoutReverseRouter extends CheckoutSimpleReverseRouter, LocalizedReverseRouter {

    default Call checkoutAddressesPageCall() {
        return checkoutAddressesPageCall(languageTag());
    }

    default Call checkoutAddressesProcessFormCall() {
        return checkoutAddressesProcessFormCall(languageTag());
    }

    default Call checkoutShippingPageCall() {
        return checkoutShippingPageCall(languageTag());
    }

    default Call checkoutShippingProcessFormCall() {
        return checkoutShippingProcessFormCall(languageTag());
    }

    default Call checkoutPaymentPageCall() {
        return checkoutPaymentPageCall(languageTag());
    }

    default Call checkoutPaymentProcessFormCall() {
        return checkoutPaymentProcessFormCall(languageTag());
    }

    default Call checkoutConfirmationPageCall() {
        return checkoutConfirmationPageCall(languageTag());
    }

    default Call checkoutConfirmationProcessFormCall() {
        return checkoutConfirmationProcessFormCall(languageTag());
    }

    default Call checkoutThankYouPageCall() {
        return checkoutThankYouPageCall(languageTag());
    }
}
