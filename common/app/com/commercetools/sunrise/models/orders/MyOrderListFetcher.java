package com.commercetools.sunrise.models.orders;

import com.commercetools.sunrise.core.controllers.ResourceFetcher;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.queries.PagedQueryResult;

import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;

@ImplementedBy(DefaultMyOrderListFetcher.class)
@FunctionalInterface
public interface MyOrderListFetcher extends ResourceFetcher<Order>, Supplier<CompletionStage<PagedQueryResult<Order>>> {

    @Override
    CompletionStage<PagedQueryResult<Order>> get();
}
