package com.commercetools.sunrise.framework.reverserouters.myaccount;

import com.commercetools.sunrise.common.injection.RequestScoped;
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
    public Call addressBookPageCall(final String languageTag) {
        return delegate.addressBookPageCall(languageTag);
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
    public Call changeAddressPageCall(final String languageTag, final String addressId) {
        return delegate.changeAddressPageCall(languageTag, addressId);
    }

    @Override
    public Call changeAddressProcessCall(final String languageTag, final String addressId) {
        return delegate.changeAddressProcessCall(languageTag, addressId);
    }

    @Override
    public Call removeAddressProcessCall(final String languageTag, final String addressId) {
        return delegate.removeAddressProcessCall(languageTag, addressId);
    }
}
