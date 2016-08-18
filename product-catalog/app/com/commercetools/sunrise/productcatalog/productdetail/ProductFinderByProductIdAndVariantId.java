package com.commercetools.sunrise.productcatalog.productdetail;

import com.commercetools.sunrise.hooks.RequestHookContext;
import com.commercetools.sunrise.hooks.requests.ProductProjectionSearchHook;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.products.ByIdVariantIdentifier;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.PagedSearchResult;
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public final class ProductFinderByProductIdAndVariantId implements ProductFinder<String, Integer> {
    @Inject
    private SphereClient sphereClient;
    @Inject
    private RequestHookContext hookContext;

    @Override
    public CompletionStage<ProductFinderResult> findProduct(final String productId, final Integer variantId) {
        final ProductProjectionSearch request = ProductProjectionSearch.ofCurrent().withQueryFilters(m -> m.id().is(productId));
        return sphereClient.execute(ProductProjectionSearchHook.runHook(hookContext, request))
                .thenApplyAsync(PagedSearchResult::head, HttpExecution.defaultContext())
                .thenApplyAsync(productOptional -> {
                    final ProductProjection product = productOptional.orElse(null);
                    final ProductVariant productVariant = productOptional
                            .flatMap(p -> p.findVariant(ByIdVariantIdentifier.of(productId, variantId)))
                            .orElse(null);
                    return ProductFinderResult.of(product, productVariant);
                }, HttpExecution.defaultContext());
    }
}
