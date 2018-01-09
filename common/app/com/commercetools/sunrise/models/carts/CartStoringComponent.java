package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.core.hooks.ctpevents.*;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.customers.CustomerSignInResult;
import io.sphere.sdk.orders.Order;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public final class CartStoringComponent implements ControllerComponent, CartLoadedHook, CartUpdatedHook, CartCreatedHook, CustomerSignInResultLoadedHook, OrderCreatedHook {

    private final MyCartInSession myCartInSession;
    private final MyCartInCache myCartInCache;

    @Inject
    CartStoringComponent(final MyCartInSession myCartInSession, final MyCartInCache myCartInCache) {
        this.myCartInSession = myCartInSession;
        this.myCartInCache = myCartInCache;
    }

    @Override
    public CompletionStage<?> onCartLoaded(final Cart cart) {
        return storeCart(cart);
    }

    @Override
    public CompletionStage<?> onCartUpdated(final Cart cart) {
        return storeCart(cart);
    }

    @Override
    public CompletionStage<?> onCartCreated(final Cart cart) {
        return storeCart(cart);
    }

    @Override
    public CompletionStage<?> onCustomerSignInResultLoaded(final CustomerSignInResult customerSignInResult) {
        return storeCart(customerSignInResult.getCart());
    }

    @Override
    public CompletionStage<?> onOrderCreated(final Order order) {
        myCartInSession.remove();
        myCartInCache.remove();
        return completedFuture(null);
    }

    private CompletionStage<?> storeCart(final Cart cart) {
        myCartInSession.store(cart);
        myCartInCache.store(cart);
        return completedFuture(null);
    }
}
