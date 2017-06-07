package com.commercetools.sunrise.framework.reverserouters.shoppingcart.checkout;

import com.commercetools.sunrise.framework.reverserouters.AbstractLocalizedReverseRouter;
import play.mvc.Call;

import javax.inject.Inject;
import java.util.Locale;

public class DefaultCheckoutReverseRouter extends AbstractLocalizedReverseRouter implements CheckoutReverseRouter {

    private final SimpleCheckoutReverseRouter delegate;

    @Inject
    protected DefaultCheckoutReverseRouter(final Locale locale, final SimpleCheckoutReverseRouter reverseRouter) {
        super(locale);
        this.delegate = reverseRouter;
    }

    @Override
    public Call checkoutAddressPageCall(final String languageTag) {
        return delegate.checkoutAddressPageCall(languageTag);
    }

    @Override
    public Call checkoutAddressProcessCall(final String languageTag) {
        return delegate.checkoutAddressProcessCall(languageTag);
    }

    @Override
    public Call checkoutShippingPageCall(final String languageTag) {
        return delegate.checkoutShippingPageCall(languageTag);
    }

    @Override
    public Call checkoutShippingProcessCall(final String languageTag) {
        return delegate.checkoutShippingProcessCall(languageTag);
    }

    @Override
    public Call checkoutPaymentPageCall(final String languageTag) {
        return delegate.checkoutPaymentPageCall(languageTag);
    }

    @Override
    public Call checkoutPaymentProcessCall(final String languageTag) {
        return delegate.checkoutPaymentProcessCall(languageTag);
    }

    @Override
    public Call checkoutConfirmationPageCall(final String languageTag) {
        return delegate.checkoutConfirmationPageCall(languageTag);
    }

    @Override
    public Call checkoutConfirmationProcessCall(final String languageTag) {
        return delegate.checkoutConfirmationProcessCall(languageTag);
    }

    @Override
    public Call checkoutThankYouPageCall(final String languageTag) {
        return delegate.checkoutThankYouPageCall(languageTag);
    }
}
