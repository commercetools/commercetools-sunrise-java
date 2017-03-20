package com.commercetools.sunrise.framework.reverserouters.shoppingcart.checkout;

import com.commercetools.sunrise.framework.reverserouters.AbstractReflectionReverseRouter;
import com.commercetools.sunrise.framework.reverserouters.ParsedRoutes;
import com.commercetools.sunrise.framework.reverserouters.ReverseCaller;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
final class SimpleCheckoutReverseRouterByReflection extends AbstractReflectionReverseRouter implements SimpleCheckoutReverseRouter {

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
    private SimpleCheckoutReverseRouterByReflection(final ParsedRoutes parsedRoutes) {
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
