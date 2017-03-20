package com.commercetools.sunrise.productcatalog.productdetail;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.framework.hooks.ctpevents.ProductVariantLoadedHook;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

final class ProductVariantFinderBySku implements ProductVariantFinder {

    private final HookRunner hookRunner;

    @Inject
    ProductVariantFinderBySku(final HookRunner hookRunner) {
        this.hookRunner = hookRunner;
    }

    @Override
    public CompletionStage<Optional<ProductVariant>> apply(final ProductProjection product, final String sku) {
        final Optional<ProductVariant> variantOpt = product.findVariantBySku(sku);
        variantOpt.ifPresent(variant -> ProductVariantLoadedHook.runHook(hookRunner, product, variant));
        return completedFuture(variantOpt);
    }
}
