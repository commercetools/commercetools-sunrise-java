package com.commercetools.sunrise.myaccount.addressbook;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.framework.hooks.ctpevents.AddressLoadedHook;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Address;

import javax.inject.Inject;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

final class AddressFinderById implements AddressFinder {

    private final HookRunner hookRunner;

    @Inject
    AddressFinderById(final HookRunner hookRunner) {
        this.hookRunner = hookRunner;
    }

    @Override
    public CompletionStage<Optional<Address>> apply(final Customer customer, final String identifier) {
        final Optional<Address> addressOpt = customer.getAddresses().stream()
                .filter(a -> Objects.equals(a.getId(), identifier))
                .findAny();
        addressOpt.ifPresent(address -> AddressLoadedHook.runHook(hookRunner, address));
        return completedFuture(addressOpt);
    }
}
