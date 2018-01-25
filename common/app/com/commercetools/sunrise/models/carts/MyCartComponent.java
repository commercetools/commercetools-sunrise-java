package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.core.hooks.application.PageDataHook;
import com.commercetools.sunrise.core.viewmodels.PageData;
import com.commercetools.sunrise.models.orders.OrderCreatorHook;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.commands.OrderFromCartCreateCommand;
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public final class MyCartComponent implements ControllerComponent, OrderCreatorHook, PageDataHook {

    private final MyCart myCart;

    @Inject
    MyCartComponent(final MyCart myCart) {
        this.myCart = myCart;
    }

    @Override
    public CompletionStage<Order> on(final OrderFromCartCreateCommand request, final Function<OrderFromCartCreateCommand, CompletionStage<Order>> nextComponent) {
        final CompletionStage<Order> orderStage = nextComponent.apply(request);
        orderStage.thenRunAsync(myCart::remove, HttpExecution.defaultContext());
        return orderStage;
    }

    @Override
    public CompletionStage<PageData> onPageDataReady(final PageData pageData) {
        return myCart.get()
                .thenApply(cartOpt -> cartOpt
                        .map(cart -> pageData.put("cart", cart))
                        .orElse(pageData));
    }
}
