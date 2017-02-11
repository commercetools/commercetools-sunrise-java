package com.commercetools.sunrise.myaccount.addressbook;

import com.commercetools.sunrise.hooks.events.AddressLoadedHook;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.common.SunriseFrameworkMyAccountController;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Address;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public abstract class SunriseAddressBookManagementController extends SunriseFrameworkMyAccountController {

    protected CompletionStage<Optional<AddressBookActionData>> findAddressBookActionData(final String addressId) {

        return customerFinder.findCustomer()
                .thenApplyAsync(customerOpt -> customerOpt
                        .flatMap(customer -> findAddress(customer, addressId)
                                .map(address -> new AddressBookActionData(customer, address))),
                        HttpExecution.defaultContext());
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

    protected final boolean isDefaultAddress(final String addressId, @Nullable final String defaultAddressId) {
        return Objects.equals(defaultAddressId, addressId);
    }
}
