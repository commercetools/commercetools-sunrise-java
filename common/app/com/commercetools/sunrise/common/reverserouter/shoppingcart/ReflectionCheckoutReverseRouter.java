package com.commercetools.sunrise.common.reverserouter.shoppingcart;

import com.commercetools.sunrise.common.reverserouter.AbstractReflectionReverseRouter;
import com.commercetools.sunrise.common.reverserouter.ReverseCaller;
import com.commercetools.sunrise.framework.ParsedRouteList;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
final class ReflectionCheckoutReverseRouter extends AbstractReflectionReverseRouter implements CheckoutSimpleReverseRouter {

    private final ReverseCaller checkoutAddressesPageCaller;
    private final ReverseCaller checkoutAddressesProcessFormCaller;
    private final ReverseCaller checkoutShippingPageCaller;
    private final ReverseCaller checkoutShippingProcessFormCaller;
    private final ReverseCaller checkoutPaymentPageCaller;
    private final ReverseCaller checkoutPaymentProcessFormCaller;
    private final ReverseCaller checkoutConfirmationPageCaller;
    private final ReverseCaller checkoutConfirmationProcessFormCaller;
    private final ReverseCaller checkoutThankYouPageCaller;

    @Inject
    private ReflectionCheckoutReverseRouter(final ParsedRouteList parsedRouteList) {
        checkoutAddressesPageCaller = getCallerForRoute(parsedRouteList, "checkoutAddressesPageCall");
        checkoutAddressesProcessFormCaller = getCallerForRoute(parsedRouteList, "checkoutAddressesProcessFormCall");
        checkoutShippingPageCaller = getCallerForRoute(parsedRouteList, "checkoutShippingPageCall");
        checkoutShippingProcessFormCaller = getCallerForRoute(parsedRouteList, "checkoutShippingProcessFormCall");
        checkoutPaymentPageCaller = getCallerForRoute(parsedRouteList, "checkoutPaymentPageCall");
        checkoutPaymentProcessFormCaller = getCallerForRoute(parsedRouteList, "checkoutPaymentProcessFormCall");
        checkoutConfirmationPageCaller = getCallerForRoute(parsedRouteList, "checkoutConfirmationPageCall");
        checkoutConfirmationProcessFormCaller = getCallerForRoute(parsedRouteList, "checkoutConfirmationProcessFormCall");
        checkoutThankYouPageCaller = getCallerForRoute(parsedRouteList, "checkoutThankYouPageCall");
    }

    @Override
    public Call checkoutAddressesPageCall(final String languageTag) {
        return checkoutAddressesPageCaller.call(languageTag);
    }

    @Override
    public Call checkoutAddressesProcessFormCall(final String languageTag) {
        return checkoutAddressesProcessFormCaller.call(languageTag);
    }

    @Override
    public Call checkoutShippingPageCall(final String languageTag) {
        return checkoutShippingPageCaller.call(languageTag);
    }

    @Override
    public Call checkoutShippingProcessFormCall(final String languageTag) {
        return checkoutShippingProcessFormCaller.call(languageTag);
    }

    @Override
    public Call checkoutPaymentPageCall(final String languageTag) {
        return checkoutPaymentPageCaller.call(languageTag);
    }

    @Override
    public Call checkoutPaymentProcessFormCall(final String languageTag) {
        return checkoutPaymentProcessFormCaller.call(languageTag);
    }

    @Override
    public Call checkoutConfirmationPageCall(final String languageTag) {
        return checkoutConfirmationPageCaller.call(languageTag);
    }

    @Override
    public Call checkoutConfirmationProcessFormCall(final String languageTag) {
        return checkoutConfirmationProcessFormCaller.call(languageTag);
    }

    @Override
    public Call checkoutThankYouPageCall(final String languageTag) {
        return checkoutThankYouPageCaller.call(languageTag);
    }
}
