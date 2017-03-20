package com.commercetools.sunrise.sessions.cart;

import com.commercetools.sunrise.framework.components.controllers.ControllerComponent;
import com.commercetools.sunrise.framework.hooks.ctpevents.CartCreatedHook;
import com.commercetools.sunrise.framework.hooks.ctpevents.CartLoadedHook;
import com.commercetools.sunrise.framework.hooks.ctpevents.CartUpdatedHook;
import com.commercetools.sunrise.framework.hooks.ctpevents.CustomerSignInResultLoadedHook;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.customers.CustomerSignInResult;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

@RequestScoped
public final class CartInSessionControllerComponent implements ControllerComponent, CartLoadedHook, CartUpdatedHook, CartCreatedHook, CustomerSignInResultLoadedHook {

    private final CartInSession cartInSession;

    @Inject
    CartInSessionControllerComponent(final CartInSession cartInSession) {
        this.cartInSession = cartInSession;
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
    public CompletionStage<?> onCartCreated(final Cart cart) {
        overwriteCartInSession(cart);
        return completedFuture(null);
    }

    @Override
    public CompletionStage<?> onCustomerSignInResultLoaded(final CustomerSignInResult customerSignInResult) {
        overwriteCartInSession(customerSignInResult.getCart());
        return completedFuture(null);
    }

    private void overwriteCartInSession(@Nullable final Cart cart) {
        cartInSession.store(cart);
    }
}
