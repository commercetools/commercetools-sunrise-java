package com.commercetools.sunrise.models.addresses;

import com.commercetools.sunrise.core.controllers.ResourceFinder;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Address;

import java.util.Optional;
import java.util.function.BiFunction;

@ImplementedBy(DefaultAddressFinder.class)
@FunctionalInterface
public interface AddressFinder extends ResourceFinder<Address>, BiFunction<Customer, String, Optional<Address>> {

    @Override
    Optional<Address> apply(final Customer customer, final String identifier);
}
