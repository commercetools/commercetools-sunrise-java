package com.commercetools.sunrise.hooks;

import io.sphere.sdk.orders.Order;

import java.util.concurrent.CompletionStage;

public interface SingleOrderHook extends Hook {
    CompletionStage<?> onSingleOrderLoaded(final Order category);
}
