package com.commercetools.sunrise.models.addresses;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Address;

import javax.inject.Inject;
import java.util.Objects;
import java.util.Optional;

final class DefaultAddressFinder extends AbstractAddressFinder {

    @Inject
    DefaultAddressFinder(final HookRunner hookRunner) {
        super(hookRunner);
    }

    @Override
    protected Optional<Address> find(final Customer customer, final String id) {
        return customer.getAddresses().stream()
                .filter(a -> Objects.equals(a.getId(), id))
                .findAny();
    }
}
