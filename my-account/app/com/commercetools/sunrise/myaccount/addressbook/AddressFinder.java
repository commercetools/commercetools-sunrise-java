package com.commercetools.sunrise.myaccount.addressbook;

import com.commercetools.sunrise.framework.controllers.ResourceFinder;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Address;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;

@ImplementedBy(AddressFinderById.class)
@FunctionalInterface
public interface AddressFinder extends ResourceFinder, BiFunction<Customer, String, CompletionStage<Optional<Address>>> {

    @Override
    CompletionStage<Optional<Address>> apply(final Customer customer, final String identifier);
}
