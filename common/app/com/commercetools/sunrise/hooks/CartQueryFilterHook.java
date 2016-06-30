package com.commercetools.sunrise.hooks;

import io.sphere.sdk.carts.queries.CartQuery;

public interface CartQueryFilterHook extends Hook {
    CartQuery filterCartQuery(CartQuery query);
}
