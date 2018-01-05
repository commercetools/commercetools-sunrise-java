package com.commercetools.sunrise.models.products;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

import javax.inject.Inject;
import java.util.Optional;

final class DefaultProductVariantFinder extends AbstractProductVariantFinder {

    @Inject
    DefaultProductVariantFinder(final HookRunner hookRunner) {
        super(hookRunner);
    }

    @Override
    protected Optional<ProductVariant> find(final ProductProjection product, final String sku) {
        return product.findVariantBySku(sku);
    }
}
