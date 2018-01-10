package com.commercetools.sunrise.models.orders;

import com.commercetools.sunrise.core.controllers.ResourceFetcher;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.queries.OrderQuery;
import io.sphere.sdk.queries.PagedQueryResult;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultMyOrderListFetcher.class)
public interface MyOrderListFetcher extends ResourceFetcher<PagedQueryResult<Order>, OrderQuery> {

    Optional<OrderQuery> defaultRequest();

    CompletionStage<PagedQueryResult<Order>> get();
}
