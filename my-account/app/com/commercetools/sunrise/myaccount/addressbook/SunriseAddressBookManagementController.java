package com.commercetools.sunrise.myaccount.addressbook;

import com.commercetools.sunrise.hooks.events.AddressLoadedHook;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.SunriseFrameworkMyAccountController;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Address;
import play.mvc.Result;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public abstract class SunriseAddressBookManagementController extends SunriseFrameworkMyAccountController {

    protected SunriseAddressBookManagementController(final CustomerFinder customerFinder) {
        super(customerFinder);
    }

    protected CompletionStage<Result> requireAddressWithCustomer(final String addressId, final Function<AddressWithCustomer, CompletionStage<Result>> nextAction) {
        return requireCustomer(customer -> findAddress(customer, addressId)
                .map(address -> AddressWithCustomer.of(address, customer))
                .map(nextAction)
                .orElseGet(this::handleNotFoundAddress));
    }

    protected Optional<Address> findAddress(final Customer customer, final String addressId) {
        return customer.getAddresses().stream()
                .filter(a -> Objects.equals(a.getId(), addressId))
                .findAny()
                .map(address -> {
                    AddressLoadedHook.runHook(hooks(), address);
                    return address;
                });
    }

    protected abstract CompletionStage<Result> handleNotFoundAddress();
}
