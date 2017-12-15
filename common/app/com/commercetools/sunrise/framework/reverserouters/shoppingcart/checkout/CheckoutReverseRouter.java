package com.commercetools.sunrise.framework.reverserouters.shoppingcart.checkout;

import com.commercetools.sunrise.framework.reverserouters.ReverseRouter;
import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(DefaultCheckoutReverseRouter.class)
public interface CheckoutReverseRouter extends ReverseRouter {

    String CHECKOUT_ADDRESS_PAGE = "checkoutAddressPageCall";

    Call checkoutAddressPageCall();

    String CHECKOUT_ADDRESS_PROCESS = "checkoutAddressProcessCall";

    Call checkoutAddressProcessCall();

    String CHECKOUT_SHIPPING_PAGE = "checkoutShippingPageCall";

    Call checkoutShippingPageCall();

    String CHECKOUT_SHIPPING_PROCESS = "checkoutShippingProcessCall";

    Call checkoutShippingProcessCall();

    String CHECKOUT_PAYMENT_PAGE = "checkoutPaymentPageCall";

    Call checkoutPaymentPageCall();

    String CHECKOUT_PAYMENT_PROCESS = "checkoutPaymentProcessCall";

    Call checkoutPaymentProcessCall();

    String CHECKOUT_CONFIRMATION_PAGE = "checkoutConfirmationPageCall";

    Call checkoutConfirmationPageCall();

    String CHECKOUT_CONFIRMATION_PROCESS = "checkoutConfirmationProcessCall";

    Call checkoutConfirmationProcessCall();

    String CHECKOUT_THANK_YOU_PAGE = "checkoutThankYouPageCall";

    Call checkoutThankYouPageCall();
}
