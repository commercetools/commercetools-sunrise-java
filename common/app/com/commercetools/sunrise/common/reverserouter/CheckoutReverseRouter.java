package com.commercetools.sunrise.common.reverserouter;

import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(ReflectionCheckoutReverseRouter.class)
public interface CheckoutReverseRouter {

    Call checkoutAddressesPageCall(final String languageTag);

    Call checkoutAddressesProcessFormCall(final String languageTag);

    Call checkoutShippingPageCall(final String languageTag);

    Call checkoutShippingProcessFormCall(final String languageTag);

    Call checkoutPaymentPageCall(final String languageTag);

    Call checkoutPaymentProcessFormCall(final String languageTag);

    Call checkoutConfirmationPageCall(final String languageTag);

    Call checkoutConfirmationProcessFormCall(final String languageTag);

    Call checkoutThankYouPageCall(final String languageTag);
}
