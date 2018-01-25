package com.commercetools.sunrise.models.orders;

import com.commercetools.sunrise.core.ResourceFetcher;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.queries.PagedQueryResult;

import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultMyOrderListFetcher.class)
public interface MyOrderListFetcher extends ResourceFetcher {

    CompletionStage<PagedQueryResult<Order>> get();
}
