package com.commercetools.sunrise.models.addresses;

import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.core.hooks.ctpevents.AddressLoadedHook;
import com.commercetools.sunrise.models.customers.MyCustomerFetcher;
import io.sphere.sdk.models.Address;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

final class DefaultMyAddressFetcher implements MyAddressFetcher {

    private final MyCustomerFetcher myCustomerFetcher;
    private final HookRunner hookRunner;

    @Inject
    DefaultMyAddressFetcher(final MyCustomerFetcher myCustomerFetcher, final HookRunner hookRunner) {
        this.myCustomerFetcher = myCustomerFetcher;
        this.hookRunner = hookRunner;
    }

    @Override
    public CompletionStage<Optional<Address>> get(final String identifier) {
        return myCustomerFetcher.get()
                .thenApply(customerOpt -> customerOpt
                        .flatMap(customer -> {
                            final Optional<Address> addressOpt = customer.findAddressById(identifier);
                            addressOpt.ifPresent(address -> AddressLoadedHook.runHook(hookRunner, address));
                            return addressOpt;
                        }));
    }
}
