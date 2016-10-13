package com.commercetools.sunrise.myaccount.addressbook;

import com.commercetools.sunrise.common.reverserouter.AddressBookReverseRouter;
import com.commercetools.sunrise.hooks.events.AddressLoadedHook;
import com.commercetools.sunrise.myaccount.common.SunriseFrameworkMyAccountController;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Address;
import org.slf4j.LoggerFactory;
import play.libs.concurrent.HttpExecution;
import play.mvc.Call;
import play.mvc.Result;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Objects;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class SunriseAddressBookManagementController extends SunriseFrameworkMyAccountController {

    @Inject
    private void postInit() {
        //just prepend another error handler if this does not suffice
        prependErrorHandler(e -> e instanceof AddressNotFoundException, e -> {
            LoggerFactory.getLogger(SunriseAddressBookManagementController.class).error("access denied", e);
            return handleNotFoundAddress();
        });
    }

    protected CompletionStage<AddressBookActionData> requireAddressBookActionData(final String addressId) {
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

    protected CompletionStage<Result> handleNotFoundAddress() {
        return redirectToAddressBook();
    }

    protected final CompletionStage<Result> redirectToAddressBook() {
        final Call call = injector().getInstance(AddressBookReverseRouter.class).addressBookCall(userContext().languageTag());
        return completedFuture(redirect(call));
    }

    protected final boolean isDefaultAddress(final String addressId, @Nullable final String defaultAddressId) {
        return Objects.equals(defaultAddressId, addressId);
    }
}
