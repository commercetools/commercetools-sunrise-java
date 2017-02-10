package com.commercetools.sunrise.common.reverserouter;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import play.mvc.Call;

import javax.inject.Inject;
import java.util.Locale;

@RequestScoped
final class ReflectionCheckoutLocalizedReverseRouter extends AbstractLocalizedReverseRouter implements CheckoutLocalizedReverseRouter {

    private final CheckoutReverseRouter delegate;

    @Inject
    private ReflectionCheckoutLocalizedReverseRouter(final Locale locale, final CheckoutReverseRouter reverseRouter) {
        super(locale);
        this.delegate = reverseRouter;
    }

    @Override
    public Call checkoutAddressesPageCall(final String languageTag) {
        return delegate.checkoutAddressesPageCall(languageTag);
    }

    @Override
    public Call checkoutAddressesProcessFormCall(final String languageTag) {
        return delegate.checkoutAddressesProcessFormCall(languageTag);
    }

    @Override
    public Call checkoutShippingPageCall(final String languageTag) {
        return delegate.checkoutShippingPageCall(languageTag);
    }

    @Override
    public Call checkoutShippingProcessFormCall(final String languageTag) {
        return delegate.checkoutShippingProcessFormCall(languageTag);
    }

    @Override
    public Call checkoutPaymentPageCall(final String languageTag) {
        return delegate.checkoutPaymentPageCall(languageTag);
    }

    @Override
    public Call checkoutPaymentProcessFormCall(final String languageTag) {
        return delegate.checkoutPaymentProcessFormCall(languageTag);
    }

    @Override
    public Call checkoutConfirmationPageCall(final String languageTag) {
        return delegate.checkoutConfirmationPageCall(languageTag);
    }

    @Override
    public Call checkoutConfirmationProcessFormCall(final String languageTag) {
        return delegate.checkoutConfirmationProcessFormCall(languageTag);
    }

    @Override
    public Call checkoutThankYouPageCall(final String languageTag) {
        return delegate.checkoutThankYouPageCall(languageTag);
    }
}
