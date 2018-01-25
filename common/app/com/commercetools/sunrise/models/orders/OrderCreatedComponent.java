package com.commercetools.sunrise.models.orders;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.core.hooks.application.HttpRequestHook;
import com.commercetools.sunrise.core.hooks.application.PageDataHook;
import com.commercetools.sunrise.core.viewmodels.PageData;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.commands.OrderFromCartCreateCommand;
import io.sphere.sdk.orders.queries.OrderByIdGet;
import play.libs.concurrent.HttpExecution;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.completedFuture;

public final class OrderCreatedComponent implements ControllerComponent, OrderCreatorHook, HttpRequestHook, PageDataHook {

    private static final String FLASH_KEY = "createdOrderId";

    private final SphereClient sphereClient;
    private CompletionStage<Order> orderStage;

    @Inject
    OrderCreatedComponent(final SphereClient sphereClient) {
        this.sphereClient = sphereClient;
    }

    @Override
    public CompletionStage<Result> on(final Http.Context ctx, final Function<Http.Context, CompletionStage<Result>> nextComponent) {
        Optional.ofNullable(Http.Context.current().flash().get(FLASH_KEY))
                .ifPresent(orderId -> orderStage = sphereClient.execute(OrderByIdGet.of(orderId)));
        return nextComponent.apply(ctx);
    }

    @Override
    public CompletionStage<Order> on(final OrderFromCartCreateCommand request, final Function<OrderFromCartCreateCommand, CompletionStage<Order>> nextComponent) {
        final CompletionStage<Order> orderStage = nextComponent.apply(request);
        orderStage.thenAcceptAsync(order -> Http.Context.current().flash().put(FLASH_KEY, order.getOrderNumber()), HttpExecution.defaultContext());
        return orderStage;
    }

    @Override
    public CompletionStage<PageData> onPageDataReady(final PageData pageData) {
        if (orderStage != null) {
            return orderStage.thenApply(order -> pageData.put("order", order));
        }
        return completedFuture(pageData);
    }
}
