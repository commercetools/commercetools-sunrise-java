package com.commercetools.sunrise.framework.reverserouters.shoppingcart.checkout;

import com.commercetools.sunrise.framework.reverserouters.AbstractReflectionReverseRouter;
import com.commercetools.sunrise.framework.reverserouters.ParsedRoutes;
import com.commercetools.sunrise.framework.reverserouters.ReverseCaller;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DefaultCheckoutReverseRouter extends AbstractReflectionReverseRouter implements CheckoutReverseRouter {

    private final ReverseCaller checkoutAddressPageCaller;
    private final ReverseCaller checkoutAddressProcessCaller;
    private final ReverseCaller checkoutShippingPageCaller;
    private final ReverseCaller checkoutShippingProcessCaller;
    private final ReverseCaller checkoutPaymentPageCaller;
    private final ReverseCaller checkoutPaymentProcessCaller;
    private final ReverseCaller checkoutConfirmationPageCaller;
    private final ReverseCaller checkoutConfirmationProcessCaller;
    private final ReverseCaller checkoutThankYouPageCaller;

    @Inject
    protected DefaultCheckoutReverseRouter(final ParsedRoutes parsedRoutes) {
        checkoutAddressPageCaller = getReverseCallerForSunriseRoute(CHECKOUT_ADDRESS_PAGE, parsedRoutes);
        checkoutAddressProcessCaller = getReverseCallerForSunriseRoute(CHECKOUT_ADDRESS_PROCESS, parsedRoutes);
        checkoutShippingPageCaller = getReverseCallerForSunriseRoute(CHECKOUT_SHIPPING_PAGE, parsedRoutes);
        checkoutShippingProcessCaller = getReverseCallerForSunriseRoute(CHECKOUT_SHIPPING_PROCESS, parsedRoutes);
        checkoutPaymentPageCaller = getReverseCallerForSunriseRoute(CHECKOUT_PAYMENT_PAGE, parsedRoutes);
        checkoutPaymentProcessCaller = getReverseCallerForSunriseRoute(CHECKOUT_PAYMENT_PROCESS, parsedRoutes);
        checkoutConfirmationPageCaller = getReverseCallerForSunriseRoute(CHECKOUT_CONFIRMATION_PAGE, parsedRoutes);
        checkoutConfirmationProcessCaller = getReverseCallerForSunriseRoute(CHECKOUT_CONFIRMATION_PROCESS, parsedRoutes);
        checkoutThankYouPageCaller = getReverseCallerForSunriseRoute(CHECKOUT_THANK_YOU_PAGE, parsedRoutes);
    }

    @Override
    public Call checkoutAddressPageCall() {
        return checkoutAddressPageCaller.call();
    }

    @Override
    public Call checkoutAddressProcessCall() {
        return checkoutAddressProcessCaller.call();
    }

    @Override
    public Call checkoutShippingPageCall() {
        return checkoutShippingPageCaller.call();
    }

    @Override
    public Call checkoutShippingProcessCall() {
        return checkoutShippingProcessCaller.call();
    }

    @Override
    public Call checkoutPaymentPageCall() {
        return checkoutPaymentPageCaller.call();
    }

    @Override
    public Call checkoutPaymentProcessCall() {
        return checkoutPaymentProcessCaller.call();
    }

    @Override
    public Call checkoutConfirmationPageCall() {
        return checkoutConfirmationPageCaller.call();
    }

    @Override
    public Call checkoutConfirmationProcessCall() {
        return checkoutConfirmationProcessCaller.call();
    }

    @Override
    public Call checkoutThankYouPageCall() {
        return checkoutThankYouPageCaller.call();
    }
}
