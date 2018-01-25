package com.commercetools.sunrise.models.addresses;

import com.commercetools.sunrise.models.customers.MyCustomerFetcher;
import io.sphere.sdk.models.Address;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

final class DefaultMyAddressFetcher implements MyAddressFetcher {

    private final MyCustomerFetcher myCustomerFetcher;

    @Inject
    DefaultMyAddressFetcher(final MyCustomerFetcher myCustomerFetcher) {
        this.myCustomerFetcher = myCustomerFetcher;
    }

    @Override
    public CompletionStage<Optional<Address>> get(final String identifier) {
        return myCustomerFetcher.get()
                .thenApply(customerOpt -> customerOpt
                        .flatMap(customer -> customer.findAddressById(identifier)));
    }
}
