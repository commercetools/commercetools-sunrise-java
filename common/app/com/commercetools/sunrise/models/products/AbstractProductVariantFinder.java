package com.commercetools.sunrise.models.products;

import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.core.hooks.ctpevents.ProductVariantLoadedHook;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

import java.util.Optional;

public abstract class AbstractProductVariantFinder implements ProductVariantFinder {

    private final HookRunner hookRunner;

    protected AbstractProductVariantFinder(final HookRunner hookRunner) {
        this.hookRunner = hookRunner;
    }

    @Override
    public Optional<ProductVariant> apply(final ProductProjection product, final String identifier) {
        final Optional<ProductVariant> variantOpt = find(product, identifier);
        variantOpt.ifPresent(variant -> ProductVariantLoadedHook.runHook(hookRunner, product, variant));
        return variantOpt;
    }

    protected abstract Optional<ProductVariant> find(final ProductProjection product, final String identifier);
}
