package com.commercetools.sunrise.myaccount;

import com.commercetools.sunrise.hooks.RequestHookContext;
import com.commercetools.sunrise.hooks.events.CustomerLoadedHook;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.queries.CustomerByIdGet;
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

final class CustomerFinderBySession implements CustomerFinder {

    private final SphereClient sphereClient;
    private final CustomerInSession customerInSession;
    private final RequestHookContext hookContext;

    @Inject
    CustomerFinderBySession(final SphereClient sphereClient, final CustomerInSession customerInSession, final RequestHookContext hookContext) {
        this.sphereClient = sphereClient;
        this.customerInSession = customerInSession;
        this.hookContext = hookContext;
    }

    @Override
    public CompletionStage<Optional<Customer>> findCustomer() {
        final CompletionStage<Optional<Customer>> customerStage = fetchCustomer();
        customerStage.thenAcceptAsync(customerOpt ->
                        customerOpt.ifPresent(customer -> CustomerLoadedHook.runHook(hookContext, customer)), HttpExecution.defaultContext());
        return customerStage;
    }

    @Override
    public CompletionStage<Optional<String>> findCustomerId() {
        return completedFuture(customerInSession.findCustomerId());
    }

    private CompletionStage<Optional<Customer>> fetchCustomer() {
        return customerInSession.findCustomerId()
                .map(this::fetchCustomerById)
                .orElseGet(() -> completedFuture(Optional.empty()));
    }

    private CompletionStage<Optional<Customer>> fetchCustomerById(final String customerId) {
        final CustomerByIdGet query = CustomerByIdGet.of(customerId);
        return sphereClient.execute(query).thenApply(Optional::ofNullable);
    }
}
