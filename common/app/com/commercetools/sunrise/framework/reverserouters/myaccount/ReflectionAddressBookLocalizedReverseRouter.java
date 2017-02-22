package com.commercetools.sunrise.framework.reverserouters.myaccount;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.reverserouters.AbstractLocalizedReverseRouter;
import play.mvc.Call;

import javax.inject.Inject;
import java.util.Locale;

@RequestScoped
final class ReflectionAddressBookLocalizedReverseRouter extends AbstractLocalizedReverseRouter implements AddressBookReverseRouter {

    private final AddressBookSimpleReverseRouter delegate;

    @Inject
    private ReflectionAddressBookLocalizedReverseRouter(final Locale locale, final AddressBookSimpleReverseRouter reverseRouter) {
        super(locale);
        this.delegate = reverseRouter;
    }

    @Override
    public Call addressBookDetailPageCall(final String languageTag) {
        return delegate.addressBookDetailPageCall(languageTag);
    }

    @Override
    public Call addAddressPageCall(final String languageTag) {
        return delegate.addAddressPageCall(languageTag);
    }

    @Override
    public Call addAddressProcessCall(final String languageTag) {
        return delegate.addAddressProcessCall(languageTag);
    }

    @Override
    public Call changeAddressPageCall(final String languageTag, final String addressIdentifier) {
        return delegate.changeAddressPageCall(languageTag, addressIdentifier);
    }

    @Override
    public Call changeAddressProcessCall(final String languageTag, final String addressIdentifier) {
        return delegate.changeAddressProcessCall(languageTag, addressIdentifier);
    }

    @Override
    public Call removeAddressProcessCall(final String languageTag, final String addressIdentifier) {
        return delegate.removeAddressProcessCall(languageTag, addressIdentifier);
    }
}
