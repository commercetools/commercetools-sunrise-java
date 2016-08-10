package com.commercetools.sunrise.myaccount;

import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.queries.CustomerByIdGet;
import play.libs.concurrent.HttpExecution;
import play.mvc.Http;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public final class CustomerFinderBySession implements CustomerFinder<Http.Session> {

    @Inject
    private SphereClient sphereClient;
    @Inject
    private CustomerSessionHandler customerSessionHandler;

    @Override
    public CompletionStage<Optional<Customer>> findCustomer(final Http.Session session) {
        final CompletionStage<Optional<Customer>> customerStage = fetchCustomer(session);
        customerStage.thenAcceptAsync(customer ->
                customerSessionHandler.overwriteInSession(session, customer.orElse(null)),
                HttpExecution.defaultContext());
        return customerStage;
    }

    private CompletionStage<Optional<Customer>> fetchCustomer(final Http.Session session) {
        return customerSessionHandler.findCustomerId(session)
                .map(this::fetchCustomerById)
                .orElseGet(() -> completedFuture(Optional.empty()));
    }

    private CompletionStage<Optional<Customer>> fetchCustomerById(final String customerId) {
        final CustomerByIdGet query = CustomerByIdGet.of(customerId);
        return sphereClient.execute(query).thenApply(Optional::ofNullable);
    }
}
