package com.commercetools.sunrise.models.orders;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.core.hooks.application.HttpRequestStartedHook;
import com.commercetools.sunrise.core.hooks.application.PageDataHook;
import com.commercetools.sunrise.core.hooks.ctpevents.OrderCreatedHook;
import com.commercetools.sunrise.core.viewmodels.PageData;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.queries.OrderByIdGet;
import play.mvc.Http;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public final class OrderCreatedComponent implements ControllerComponent, OrderCreatedHook, HttpRequestStartedHook, PageDataHook {

    private static final String FLASH_KEY = "createdOrderId";

    private final SphereClient sphereClient;
    private CompletionStage<Order> orderStage;

    @Inject
    OrderCreatedComponent(final SphereClient sphereClient) {
        this.sphereClient = sphereClient;
    }

    @Override
    public void onOrderCreated(final Order order) {
        Http.Context.current().flash().put(FLASH_KEY, order.getOrderNumber());
    }

    @Override
    public void onHttpRequestStarted(final Http.Context httpContext) {
        Optional.ofNullable(Http.Context.current().flash().get(FLASH_KEY))
                .ifPresent(orderId -> orderStage = sphereClient.execute(OrderByIdGet.of(orderId)));
    }

    @Override
    public CompletionStage<PageData> onPageDataReady(final PageData pageData) {
        if (orderStage != null) {
            return orderStage.thenApply(order -> pageData.put("order", order));
        }
        return completedFuture(pageData);
    }
}
