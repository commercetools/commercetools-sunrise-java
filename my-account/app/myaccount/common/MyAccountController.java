package myaccount.common;

import common.actions.NoCache;
import common.controllers.ControllerDependency;
import common.controllers.SunriseController;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.queries.CustomerByIdGet;
import myaccount.CustomerSessionUtils;
import play.libs.concurrent.HttpExecution;
import play.mvc.Http;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@NoCache
public abstract class MyAccountController extends SunriseController {

    public MyAccountController(final ControllerDependency controllerDependency) {
        super(controllerDependency);
    }

    protected CompletionStage<Optional<Customer>> getCustomer(final Http.Session session) {
        final CompletionStage<Optional<Customer>> customerFuture = fetchCustomer(session);
        customerFuture.thenAcceptAsync(customer ->
                CustomerSessionUtils.overwriteCustomerSessionData(customer.orElse(null), session), HttpExecution.defaultContext());
        return customerFuture;
    }

    private CompletionStage<Optional<Customer>> fetchCustomer(final Http.Session session) {
        return CustomerSessionUtils.getCustomerId(session)
                .map(this::fetchCustomerById)
                .orElseGet(() -> CompletableFuture.completedFuture(Optional.empty()));
    }

    private CompletionStage<Optional<Customer>> fetchCustomerById(final String customerId) {
        final CustomerByIdGet query = CustomerByIdGet.of(customerId);
        return sphere().execute(query).thenApply(Optional::ofNullable);
    }
}
