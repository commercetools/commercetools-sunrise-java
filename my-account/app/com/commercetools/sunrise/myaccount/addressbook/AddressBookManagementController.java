package com.commercetools.sunrise.myaccount.addressbook;

import com.commercetools.sunrise.common.forms.UserFeedback;
import com.commercetools.sunrise.common.reverserouter.AddressBookReverseRouter;
import com.commercetools.sunrise.myaccount.common.MyAccountController;
import com.google.inject.Injector;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.libs.concurrent.HttpExecution;
import play.mvc.Call;
import play.mvc.Result;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class AddressBookManagementController extends MyAccountController {

    protected static final Logger logger = LoggerFactory.getLogger(AddressBookManagementController.class);

    @Inject
    private Injector injector;

    protected CompletionStage<Result> redirectToAddressBook() {
        final Call call = injector.getInstance(AddressBookReverseRouter.class).showMyAddressBook(userContext().languageTag());
        return completedFuture(redirect(call));
    }

    protected CompletionStage<Result> handleNotFoundAddress(final Customer customer) {
        return redirectToAddressBook();
    }

    protected CompletionStage<Result> ifValidAddress(final Customer customer, @Nullable final Address address,
                                                     final Function<Address, CompletionStage<Result>> onValidAddress) {
        return Optional.ofNullable(address)
                .map(notNullAddress -> runHookOnFoundAddress(notNullAddress)
                        .thenComposeAsync(unused -> onValidAddress.apply(address), HttpExecution.defaultContext()))
                .orElseGet(() -> handleNotFoundAddress(customer));
    }

    protected final void saveFormErrors(final Form<?> form) {
        injector.getInstance(UserFeedback.class).addErrors(form);
    }

    protected final void saveUnexpectedError(final Throwable throwable) {
        logger.error("The CTP request raised an unexpected exception", throwable);
        injector.getInstance(UserFeedback.class).addErrors("Something went wrong, please try again"); // TODO get from i18n
    }

    protected final CompletionStage<?> runHookOnFoundAddress(final Address address) {
        //return runAsyncHook(SingleCustomerHook.class, hook -> hook.onSingleCustomerLoaded(customer));
        return completedFuture(null);
    }

    protected final Optional<Address> findAddress(@Nullable final Customer customer, final String addressId) {
        return Optional.ofNullable(customer)
                .flatMap(c -> c.getAddresses().stream()
                        .filter(a -> Objects.equals(a.getId(), addressId))
                        .findFirst());
    }
}
