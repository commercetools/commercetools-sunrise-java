package com.commercetools.sunrise.common.reverserouter;

import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(ReflectionCheckoutLocalizedReverseRouter.class)
public interface CheckoutLocalizedReverseRouter extends CheckoutReverseRouter, LocalizedReverseRouter {

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
