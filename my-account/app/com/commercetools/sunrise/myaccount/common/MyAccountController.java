package com.commercetools.sunrise.myaccount.common;

import com.commercetools.sunrise.common.cache.NoCache;
import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.controllers.ReverseRouter;
import com.commercetools.sunrise.common.controllers.SunriseFrameworkController;
import com.commercetools.sunrise.myaccount.CustomerSessionUtils;
import com.google.inject.Injector;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.queries.CustomerByIdGet;
import play.libs.concurrent.HttpExecution;
import play.mvc.Call;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.completedFuture;

@NoCache
public abstract class MyAccountController extends SunriseFrameworkController {

    @Inject
    private Injector injector;

    @Override
    public Set<String> getFrameworkTags() {
        return new HashSet<>(asList("my-account"));
    }

    protected CompletionStage<Optional<Customer>> getCustomer(final Http.Session session) {
        final CompletionStage<Optional<Customer>> customerFuture = fetchCustomer(session);
        customerFuture.thenAcceptAsync(customer ->
                CustomerSessionUtils.overwriteCustomerSessionData(customer.orElse(null), session), HttpExecution.defaultContext());
        return customerFuture;
    }

    protected CompletionStage<Result> getCustomerAndExecute(final Function<Customer, CompletionStage<Result>> function) {
        return getCustomer(session())
                .thenComposeAsync(customer -> customer
                                .map(c -> runHookOnFoundCustomer(c)
                                        .thenComposeAsync(unused -> function.apply(c), HttpExecution.defaultContext()))
                                .orElseGet(this::handleNotFoundCustomer),
                        HttpExecution.defaultContext());
    }

    protected CompletionStage<Optional<Customer>> fetchCustomer(final Http.Session session) {
        return CustomerSessionUtils.getCustomerId(session)
                .map(this::fetchCustomerById)
                .orElseGet(() -> CompletableFuture.completedFuture(Optional.empty()));
    }

    protected CompletionStage<Optional<Customer>> fetchCustomerById(final String customerId) {
        final CustomerByIdGet query = CustomerByIdGet.of(customerId);
        return sphere().execute(query).thenApply(Optional::ofNullable);
    }

    protected CompletionStage<Result> handleNotFoundCustomer() {
        final UserContext userContext = injector.getInstance(UserContext.class);
        final ReverseRouter reverseRouter = injector.getInstance(ReverseRouter.class);
        final Call call = reverseRouter.showLogInForm(userContext.languageTag());
        return completedFuture(redirect(call));
    }

    protected final CompletionStage<?> runHookOnFoundCustomer(final Customer customer) {
        //return runAsyncHook(SingleCustomerHook.class, hook -> hook.onSingleCustomerLoaded(customer));
        return completedFuture(null);
    }
}
