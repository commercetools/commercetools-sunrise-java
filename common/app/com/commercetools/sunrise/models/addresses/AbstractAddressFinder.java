package com.commercetools.sunrise.models.addresses;

import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.core.hooks.ctpevents.AddressLoadedHook;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Address;

import java.util.Optional;

public abstract class AbstractAddressFinder implements AddressFinder {

    private final HookRunner hookRunner;

    protected AbstractAddressFinder(final HookRunner hookRunner) {
        this.hookRunner = hookRunner;
    }

    @Override
    public Optional<Address> apply(final Customer customer, final String identifier) {
        final Optional<Address> addressOpt = find(customer, identifier);
        addressOpt.ifPresent(address -> AddressLoadedHook.runHook(hookRunner, address));
        return addressOpt;
    }

    protected abstract Optional<Address> find(final Customer customer, final String identifier);
}
