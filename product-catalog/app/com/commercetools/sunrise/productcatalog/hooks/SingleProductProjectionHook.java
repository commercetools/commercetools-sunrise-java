package com.commercetools.sunrise.productcatalog.hooks;

import com.commercetools.sunrise.common.hooks.Hook;
import io.sphere.sdk.products.ProductProjection;

import java.util.concurrent.CompletionStage;

public interface SingleProductProjectionHook extends Hook {
    CompletionStage<?> onSingleProductProjectionLoaded(final ProductProjection product);
}
