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

public class CustomerFinderBySession implements CustomerFinder {

    private final CustomerInSession customerInSession;
    private final SphereClient sphereClient;
    private final RequestHookContext hookContext;

    @Inject
    protected CustomerFinderBySession(final CustomerInSession customerInSession, final SphereClient sphereClient,
                                      final RequestHookContext hookContext) {
        this.sphereClient = sphereClient;
        this.customerInSession = customerInSession;
        this.hookContext = hookContext;
    }

    @Override
    public CompletionStage<Optional<Customer>> get() {
        return buildRequest()
                .map(request -> sphereClient.execute(request)
                        .thenApply(Optional::ofNullable)
                        .thenApplyAsync(customer -> {
                            customer.ifPresent(this::runHookOnCustomerLoaded);
                            return customer;
                        }, HttpExecution.defaultContext()))
                .orElseGet(() -> completedFuture(Optional.empty()));
    }

    protected Optional<CustomerByIdGet> buildRequest() {
        return customerInSession.findCustomerId()
                .map(CustomerByIdGet::of);
    }

    private CompletionStage<?> runHookOnCustomerLoaded(final Customer customer) {
        return CustomerLoadedHook.runHook(hookContext, customer);
    }
}
