package com.commercetools.sunrise.hooks;

import io.sphere.sdk.orders.queries.OrderByIdGet;

public interface OrderByIdGetFilterHook extends Hook {
    OrderByIdGet filterOrderByIdGet(OrderByIdGet query);
}
