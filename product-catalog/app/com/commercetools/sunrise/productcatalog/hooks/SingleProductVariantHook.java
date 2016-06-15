package com.commercetools.sunrise.productcatalog.hooks;

import com.commercetools.sunrise.hooks.Hook;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

import java.util.concurrent.CompletionStage;

public interface SingleProductVariantHook extends Hook {
    CompletionStage<?> onSingleProductVariantLoaded(final ProductProjection product, final ProductVariant variant);
}
