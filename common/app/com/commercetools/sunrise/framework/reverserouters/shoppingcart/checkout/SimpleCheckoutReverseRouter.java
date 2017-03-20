package com.commercetools.sunrise.framework.reverserouters.shoppingcart.checkout;

import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(SimpleCheckoutReverseRouterByReflection.class)
public interface SimpleCheckoutReverseRouter {

    String CHECKOUT_ADDRESS_PAGE = "checkoutAddressPageCall";

    Call checkoutAddressPageCall(final String languageTag);

    String CHECKOUT_ADDRESS_PROCESS = "checkoutAddressProcessCall";

    Call checkoutAddressProcessCall(final String languageTag);

    String CHECKOUT_SHIPPING_PAGE = "checkoutShippingPageCall";

    Call checkoutShippingPageCall(final String languageTag);

    String CHECKOUT_SHIPPING_PROCESS = "checkoutShippingProcessCall";

    Call checkoutShippingProcessCall(final String languageTag);

    String CHECKOUT_PAYMENT_PAGE = "checkoutPaymentPageCall";

    Call checkoutPaymentPageCall(final String languageTag);

    String CHECKOUT_PAYMENT_PROCESS = "checkoutPaymentProcessCall";

    Call checkoutPaymentProcessCall(final String languageTag);

    String CHECKOUT_CONFIRMATION_PAGE = "checkoutConfirmationPageCall";

    Call checkoutConfirmationPageCall(final String languageTag);

    String CHECKOUT_CONFIRMATION_PROCESS = "checkoutConfirmationProcessCall";

    Call checkoutConfirmationProcessCall(final String languageTag);

    String CHECKOUT_THANK_YOU_PAGE = "checkoutThankYouPageCall";

    Call checkoutThankYouPageCall(final String languageTag);
}
