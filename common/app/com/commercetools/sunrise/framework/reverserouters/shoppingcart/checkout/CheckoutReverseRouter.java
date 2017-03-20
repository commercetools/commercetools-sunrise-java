package com.commercetools.sunrise.framework.reverserouters.shoppingcart.checkout;

import com.commercetools.sunrise.framework.reverserouters.LocalizedReverseRouter;
import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(DefaultCheckoutReverseRouter.class)
public interface CheckoutReverseRouter extends SimpleCheckoutReverseRouter, LocalizedReverseRouter {

    default Call checkoutAddressPageCall() {
        return checkoutAddressPageCall(locale().toLanguageTag());
    }

    default Call checkoutAddressProcessCall() {
        return checkoutAddressProcessCall(locale().toLanguageTag());
    }

    default Call checkoutShippingPageCall() {
        return checkoutShippingPageCall(locale().toLanguageTag());
    }

    default Call checkoutShippingProcessCall() {
        return checkoutShippingProcessCall(locale().toLanguageTag());
    }

    default Call checkoutPaymentPageCall() {
        return checkoutPaymentPageCall(locale().toLanguageTag());
    }

    default Call checkoutPaymentProcessCall() {
        return checkoutPaymentProcessCall(locale().toLanguageTag());
    }

    default Call checkoutConfirmationPageCall() {
        return checkoutConfirmationPageCall(locale().toLanguageTag());
    }

    default Call checkoutConfirmationProcessCall() {
        return checkoutConfirmationProcessCall(locale().toLanguageTag());
    }

    default Call checkoutThankYouPageCall() {
        return checkoutThankYouPageCall(locale().toLanguageTag());
    }
}
