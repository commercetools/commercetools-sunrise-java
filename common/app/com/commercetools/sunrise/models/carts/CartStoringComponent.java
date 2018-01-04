package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.core.hooks.ctpevents.CartCreatedHook;
import com.commercetools.sunrise.core.hooks.ctpevents.CartLoadedHook;
import com.commercetools.sunrise.core.hooks.ctpevents.CartUpdatedHook;
import com.commercetools.sunrise.core.hooks.ctpevents.CustomerSignInResultLoadedHook;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.customers.CustomerSignInResult;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public final class CartStoringComponent implements ControllerComponent, CartLoadedHook, CartUpdatedHook, CartCreatedHook, CustomerSignInResultLoadedHook {

    private final CartInSession cartInSession;
    private final CartInCache cartInCache;

    @Inject
    CartStoringComponent(final CartInSession cartInSession, final CartInCache cartInCache) {
        this.cartInSession = cartInSession;
        this.cartInCache = cartInCache;
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

    private CompletionStage<?> storeCart(final Cart cart) {
        cartInSession.store(cart);
        cartInCache.store(cart);
        return completedFuture(null);
    }
}
