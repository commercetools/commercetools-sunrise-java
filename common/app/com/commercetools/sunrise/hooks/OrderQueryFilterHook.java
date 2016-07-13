package com.commercetools.sunrise.hooks;

import io.sphere.sdk.orders.queries.OrderQuery;

public interface OrderQueryFilterHook extends QueryFilterHook<OrderQuery> {

    @Override
    OrderQuery filterQuery(OrderQuery query);
}
