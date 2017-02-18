package com.commercetools.sunrise.framework.reverserouters.shoppingcart;

import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(ReflectionCheckoutReverseRouter.class)
interface CheckoutSimpleReverseRouter {

    String CHECKOUT_ADDRESS_PAGE = "checkoutAddressPage";

    Call checkoutAddressPageCall(final String languageTag);

    String CHECKOUT_ADDRESS_PROCESS = "checkoutAddressProcess";

    Call checkoutAddressProcessCall(final String languageTag);

    String CHECKOUT_SHIPPING_PAGE = "checkoutShippingPage";

    Call checkoutShippingPageCall(final String languageTag);

    String CHECKOUT_SHIPPING_PROCESS = "checkoutShippingProcess";

    Call checkoutShippingProcessCall(final String languageTag);

    String CHECKOUT_PAYMENT_PAGE = "checkoutPaymentPage";

    Call checkoutPaymentPageCall(final String languageTag);

    String CHECKOUT_PAYMENT_PROCESS = "checkoutPaymentProcess";

    Call checkoutPaymentProcessCall(final String languageTag);

    String CHECKOUT_CONFIRMATION_PAGE = "checkoutConfirmationPage";

    Call checkoutConfirmationPageCall(final String languageTag);

    String CHECKOUT_CONFIRMATION_PROCESS = "checkoutConfirmationProcess";

    Call checkoutConfirmationProcessCall(final String languageTag);

    String CHECKOUT_THANK_YOU_PAGE = "checkoutThankYouPage";

    Call checkoutThankYouPageCall(final String languageTag);
}
