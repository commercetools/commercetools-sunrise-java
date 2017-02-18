package com.commercetools.sunrise.framework.reverserouters.shoppingcart;

import com.commercetools.sunrise.framework.reverserouters.LocalizedReverseRouter;
import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(ReflectionCheckoutLocalizedReverseRouter.class)
public interface CheckoutReverseRouter extends CheckoutSimpleReverseRouter, LocalizedReverseRouter {

    default Call checkoutAddressPageCall() {
        return checkoutAddressPageCall(languageTag());
    }

    default Call checkoutAddressProcessCall() {
        return checkoutAddressProcessCall(languageTag());
    }

    default Call checkoutShippingPageCall() {
        return checkoutShippingPageCall(languageTag());
    }

    default Call checkoutShippingProcessCall() {
        return checkoutShippingProcessCall(languageTag());
    }

    default Call checkoutPaymentPageCall() {
        return checkoutPaymentPageCall(languageTag());
    }

    default Call checkoutPaymentProcessCall() {
        return checkoutPaymentProcessCall(languageTag());
    }

    default Call checkoutConfirmationPageCall() {
        return checkoutConfirmationPageCall(languageTag());
    }

    default Call checkoutConfirmationProcessCall() {
        return checkoutConfirmationProcessCall(languageTag());
    }

    default Call checkoutThankYouPageCall() {
        return checkoutThankYouPageCall(languageTag());
    }
}
