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

public final class CustomerFinderBySession implements CustomerFinder<Void> {

    @Inject
    private SphereClient sphereClient;
    @Inject
    private CustomerInSession customerInSession;
    @Inject
    private RequestHookContext hookContext;

    @Override
    public CompletionStage<Optional<Customer>> findCustomer(final Void unused) {
        final CompletionStage<Optional<Customer>> customerStage = fetchCustomer();
        customerStage.thenAcceptAsync(customerOpt ->
                        customerOpt.ifPresent(customer -> CustomerLoadedHook.runHook(hookContext, customer)), HttpExecution.defaultContext());
        return customerStage;
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
