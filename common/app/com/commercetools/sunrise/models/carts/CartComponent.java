package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.core.hooks.application.PageDataHook;
import com.commercetools.sunrise.core.hooks.ctpevents.*;
import com.commercetools.sunrise.core.viewmodels.PageData;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.customers.CustomerSignInResult;
import io.sphere.sdk.orders.Order;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public final class CartComponent implements ControllerComponent, CartLoadedHook, CartUpdatedHook, CartCreatedHook, CustomerSignInResultLoadedHook, OrderCreatedHook, PageDataHook {

    private final MyCartInSession myCartInSession;
    private final MyCartInCache myCartInCache;

    @Inject
    CartComponent(final MyCartInSession myCartInSession, final MyCartInCache myCartInCache) {
        this.myCartInSession = myCartInSession;
        this.myCartInCache = myCartInCache;
    }

    @Override
    public void onCartLoaded(final Cart cart) {
        storeCart(cart);
    }

    @Override
    public void onCartUpdated(final Cart cart) {
        storeCart(cart);
    }

    @Override
    public void onCartCreated(final Cart cart) {
        storeCart(cart);
    }

    @Override
    public void onCustomerSignInResultLoaded(final CustomerSignInResult customerSignInResult) {
        storeCart(customerSignInResult.getCart());
    }

    @Override
    public void onOrderCreated(final Order order) {
        myCartInSession.remove();
        myCartInCache.remove();
    }

    @Override
    public CompletionStage<PageData> onPageDataReady(final PageData pageData) {
        return myCartInCache.get()
                .thenApply(cartOpt -> cartOpt
                        .map(cart -> pageData.put("cart", cart))
                        .orElse(pageData));
    }

    private void storeCart(final Cart cart) {
        myCartInSession.store(cart);
        myCartInCache.store(cart);
    }
}
