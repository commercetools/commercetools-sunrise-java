package com.commercetools.sunrise.myaccount.addressbook;

import com.commercetools.sunrise.common.reverserouter.AddressBookLocalizedReverseRouter;
import com.commercetools.sunrise.hooks.events.AddressLoadedHook;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import com.commercetools.sunrise.myaccount.common.SunriseFrameworkMyAccountController;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Address;
import org.slf4j.LoggerFactory;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Objects;
import java.util.concurrent.CompletionStage;

public abstract class SunriseAddressBookManagementController extends SunriseFrameworkMyAccountController {

    private final AddressBookLocalizedReverseRouter addressBookReverseRouter;

    protected SunriseAddressBookManagementController(final AddressBookLocalizedReverseRouter addressBookReverseRouter) {
        this.addressBookReverseRouter = addressBookReverseRouter;
    }

    protected CompletionStage<AddressBookActionData> requireAddressBookActionData(final CustomerFinder customerFinder, final String addressId) {
        customerFinder.findCustomer()
                .thenComposeAsync(customerOpt -> customerOpt.map(customer -> requireAddress(customer, addressId)).orElseGet())
        return requireExistingCustomer()
                .thenApplyAsync(customer -> {
                    final Address address = requireAddress(customer, addressId);
                    return new AddressBookActionData(customer, address);
                }, HttpExecution.defaultContext());
    }

    protected Address requireAddress(final Customer customer, final String addressId) {
        final Address address = customer.getAddresses().stream()
                .filter(a -> Objects.equals(a.getId(), addressId))
                .findAny()
                .orElseThrow(AddressNotFoundException::new);
        AddressLoadedHook.runHook(hooks(), address);
        return address;
    }

    protected abstract CompletionStage<Result> handleNotFoundAddress();

    protected final boolean isDefaultAddress(final String addressId, @Nullable final String defaultAddressId) {
        return Objects.equals(defaultAddressId, addressId);
    }
}
