package com.commercetools.sunrise.framework.reverserouters.shoppingcart;

import com.commercetools.sunrise.framework.reverserouters.AbstractReflectionReverseRouter;
import com.commercetools.sunrise.framework.reverserouters.ParsedRouteList;
import com.commercetools.sunrise.framework.reverserouters.ReverseCaller;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
final class ReflectionCheckoutReverseRouter extends AbstractReflectionReverseRouter implements CheckoutSimpleReverseRouter {

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
    private ReflectionCheckoutReverseRouter(final ParsedRouteList parsedRouteList) {
        checkoutAddressPageCaller = getCallerForRoute(parsedRouteList, CHECKOUT_ADDRESS_PAGE);
        checkoutAddressProcessCaller = getCallerForRoute(parsedRouteList, CHECKOUT_ADDRESS_PROCESS);
        checkoutShippingPageCaller = getCallerForRoute(parsedRouteList, CHECKOUT_SHIPPING_PAGE);
        checkoutShippingProcessCaller = getCallerForRoute(parsedRouteList, CHECKOUT_SHIPPING_PROCESS);
        checkoutPaymentPageCaller = getCallerForRoute(parsedRouteList, CHECKOUT_PAYMENT_PAGE);
        checkoutPaymentProcessCaller = getCallerForRoute(parsedRouteList, CHECKOUT_PAYMENT_PROCESS);
        checkoutConfirmationPageCaller = getCallerForRoute(parsedRouteList, CHECKOUT_CONFIRMATION_PAGE);
        checkoutConfirmationProcessCaller = getCallerForRoute(parsedRouteList, CHECKOUT_CONFIRMATION_PROCESS);
        checkoutThankYouPageCaller = getCallerForRoute(parsedRouteList, CHECKOUT_THANK_YOU_PAGE);
    }

    @Override
    public Call checkoutAddressPageCall(final String languageTag) {
        return checkoutAddressPageCaller.call(languageTag);
    }

    @Override
    public Call checkoutAddressProcessCall(final String languageTag) {
        return checkoutAddressProcessCaller.call(languageTag);
    }

    @Override
    public Call checkoutShippingPageCall(final String languageTag) {
        return checkoutShippingPageCaller.call(languageTag);
    }

    @Override
    public Call checkoutShippingProcessCall(final String languageTag) {
        return checkoutShippingProcessCaller.call(languageTag);
    }

    @Override
    public Call checkoutPaymentPageCall(final String languageTag) {
        return checkoutPaymentPageCaller.call(languageTag);
    }

    @Override
    public Call checkoutPaymentProcessCall(final String languageTag) {
        return checkoutPaymentProcessCaller.call(languageTag);
    }

    @Override
    public Call checkoutConfirmationPageCall(final String languageTag) {
        return checkoutConfirmationPageCaller.call(languageTag);
    }

    @Override
    public Call checkoutConfirmationProcessCall(final String languageTag) {
        return checkoutConfirmationProcessCaller.call(languageTag);
    }

    @Override
    public Call checkoutThankYouPageCall(final String languageTag) {
        return checkoutThankYouPageCaller.call(languageTag);
    }
}
