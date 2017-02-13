package com.commercetools.sunrise.productcatalog.productdetail;

import com.commercetools.sunrise.hooks.HookRunner;
import com.commercetools.sunrise.hooks.events.ProductVariantLoadedHook;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

final class ProductVariantFinderBySlug implements ProductVariantFinder {

    private final HookRunner hookRunner;

    @Inject
    ProductVariantFinderBySlug(final HookRunner hookRunner) {
        this.hookRunner = hookRunner;
    }

    @Override
    public CompletionStage<Optional<ProductVariant>> apply(final ProductProjection product, final String identifier) {
        final Optional<ProductVariant> variantOpt = product.findVariantBySku(identifier);
        variantOpt.ifPresent(variant -> ProductVariantLoadedHook.runHook(hookRunner, product, variant));
        return completedFuture(variantOpt);
    }
}
