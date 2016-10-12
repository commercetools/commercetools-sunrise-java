package com.commercetools.sunrise.myaccount;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.framework.ControllerComponent;
import com.commercetools.sunrise.hooks.events.CustomerSignInResultLoadedHook;
import com.commercetools.sunrise.hooks.events.CustomerUpdatedHook;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.CustomerSignInResult;
import play.mvc.Http;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

@RequestScoped
public class CustomerComponent implements ControllerComponent, CustomerSignInResultLoadedHook, CustomerUpdatedHook {

    @Inject
    private CustomerSessionHandler customerSessionHandler;
    @Inject
    private Http.Context httpContext;

    @Override
    public CompletionStage<?> onCustomerSignInResultLoaded(final CustomerSignInResult customerSignInResult) {
        overwriteCustomerInSession(customerSignInResult.getCustomer());
        return completedFuture(null);
    }

    @Override
    public CompletionStage<?> onCustomerUpdated(final Customer customer) {
        overwriteCustomerInSession(customer);
        return completedFuture(null);
    }

    private void overwriteCustomerInSession(final Customer customer) {
        customerSessionHandler.overwriteInSession(httpContext.session(), customer);
    }
}
