package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.core.hooks.application.PageDataHook;
import com.commercetools.sunrise.core.hooks.ctpevents.OrderCreatedHook;
import com.commercetools.sunrise.core.viewmodels.PageData;
import io.sphere.sdk.orders.Order;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public final class MyCartComponent implements ControllerComponent, OrderCreatedHook, PageDataHook {

    private final MyCartInCache myCartInCache;

    @Inject
    MyCartComponent(final MyCartInCache myCartInCache) {
        this.myCartInCache = myCartInCache;
    }

    @Override
    public void onOrderCreated(final Order order) {
        myCartInCache.remove();
    }

    @Override
    public CompletionStage<PageData> onPageDataReady(final PageData pageData) {
        return myCartInCache.get()
                .thenApply(cartOpt -> cartOpt
                        .map(cart -> pageData.put("cart", cart))
                        .orElse(pageData));
    }
}
