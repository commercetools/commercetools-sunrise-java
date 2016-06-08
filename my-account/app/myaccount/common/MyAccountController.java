package myaccount.common;

import common.actions.NoCache;
import common.controllers.SunriseFrameworkController;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.queries.CustomerByIdGet;
import myaccount.CustomerSessionUtils;
import play.libs.concurrent.HttpExecution;
import play.mvc.Http;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;

@NoCache
public abstract class MyAccountController extends SunriseFrameworkController {

    protected CompletionStage<Optional<Customer>> getCustomer(final Http.Session session) {
        final CompletionStage<Optional<Customer>> customerFuture = fetchCustomer(session);
        customerFuture.thenAcceptAsync(customer ->
                CustomerSessionUtils.overwriteCustomerSessionData(customer.orElse(null), session), HttpExecution.defaultContext());
        return customerFuture;
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

    @Override
    public Set<String> getFrameworkTags() {
        return new HashSet<>(asList("my-account"));
    }
}
