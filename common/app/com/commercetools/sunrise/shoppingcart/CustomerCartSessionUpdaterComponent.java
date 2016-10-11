package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.framework.ControllerComponent;
import com.commercetools.sunrise.hooks.events.CartLoadedHook;
import com.commercetools.sunrise.hooks.events.CartUpdatedHook;
import com.commercetools.sunrise.hooks.events.CustomerSignInResultLoadedHook;
import com.commercetools.sunrise.hooks.events.CustomerUpdatedHook;
import com.commercetools.sunrise.myaccount.CustomerSessionHandler;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.CustomerSignInResult;
import play.mvc.Http;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class CustomerCartSessionUpdaterComponent implements ControllerComponent, CustomerSignInResultLoadedHook, CartLoadedHook, CartUpdatedHook, CustomerUpdatedHook {

    @Inject
    private CartSessionHandler cartSessionHandler;
    @Inject
    private CustomerSessionHandler customerSessionHandler;
    @Inject
    private Http.Context httpContext;

    @Override
    public CompletionStage<?> onCustomerSignInResultLoaded(final CustomerSignInResult customerSignInResult) {
        overwriteCartInSession(customerSignInResult.getCart());
        overwriteCustomerInSession(customerSignInResult.getCustomer());
        return completedFuture(null);
    }

    @Override
    public CompletionStage<?> onCartLoaded(final Cart cart) {
        overwriteCartInSession(cart);
        return completedFuture(null);
    }

    @Override
    public CompletionStage<?> onCartUpdated(final Cart cart) {
        overwriteCartInSession(cart);
        return completedFuture(null);
    }

    @Override
    public CompletionStage<?> onCustomerUpdated(final Customer customer) {
        overwriteCustomerInSession(customer);
        return completedFuture(null);
    }

    private void overwriteCartInSession(final Cart cart) {
        cartSessionHandler.overwriteInSession(httpContext.session(), cart);
    }

    private void overwriteCustomerInSession(final Customer customer) {
        customerSessionHandler.overwriteInSession(httpContext.session(), customer);
    }
}
