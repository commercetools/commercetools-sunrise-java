package com.commercetools.sunrise.framework.reverserouters.myaccount.addressbook;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.reverserouters.AbstractLocalizedReverseRouter;
import io.sphere.sdk.models.Address;
import play.mvc.Call;

import javax.inject.Inject;
import java.util.Locale;
import java.util.Optional;

@RequestScoped
public class DefaultAddressBookReverseRouter extends AbstractLocalizedReverseRouter implements AddressBookReverseRouter {

    private final SimpleAddressBookReverseRouter delegate;

    @Inject
    protected DefaultAddressBookReverseRouter(final Locale locale, final SimpleAddressBookReverseRouter reverseRouter) {
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

    /**
     * {@inheritDoc}
     * It uses as address identifier the address ID.
     */
    @Override
    public Optional<Call> changeAddressPageCall(final Address address) {
        return Optional.of(changeAddressPageCall(address.getId()));
    }

    /**
     * {@inheritDoc}
     * It uses as address identifier the address ID.
     */
    @Override
    public Optional<Call> changeAddressProcessCall(final Address address) {
        return Optional.of(changeAddressProcessCall(address.getId()));
    }

    /**
     * {@inheritDoc}
     * It uses as address identifier the address ID.
     */
    @Override
    public Optional<Call> removeAddressProcessCall(final Address address) {
        return Optional.of(removeAddressProcessCall(address.getId()));
    }
}
