package com.commercetools.sunrise.common.reverserouter;

import com.commercetools.sunrise.common.injection.RequestScoped;
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
    public Call addressBookCall(final String languageTag) {
        return delegate.addressBookCall(languageTag);
    }

    @Override
    public Call addAddressToAddressBookCall(final String languageTag) {
        return delegate.addAddressToAddressBookCall(languageTag);
    }

    @Override
    public Call addAddressToAddressBookProcessFormCall(final String languageTag) {
        return delegate.addAddressToAddressBookProcessFormCall(languageTag);
    }

    @Override
    public Call changeAddressInAddressBookCall(final String languageTag, final String addressId) {
        return delegate.changeAddressInAddressBookCall(languageTag, addressId);
    }

    @Override
    public Call changeAddressInAddressBookProcessFormCall(final String languageTag, final String addressId) {
        return delegate.changeAddressInAddressBookProcessFormCall(languageTag, addressId);
    }

    @Override
    public Call removeAddressFromAddressBookProcessFormCall(final String languageTag, final String addressId) {
        return delegate.removeAddressFromAddressBookProcessFormCall(languageTag, addressId);
    }
}
