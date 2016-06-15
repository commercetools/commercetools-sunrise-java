package com.commercetools.sunrise.hooks;

import com.commercetools.sunrise.hooks.Hook;
import io.sphere.sdk.products.ProductProjection;

import java.util.concurrent.CompletionStage;

public interface SingleProductProjectionHook extends Hook {
    CompletionStage<?> onSingleProductProjectionLoaded(final ProductProjection product);
}
