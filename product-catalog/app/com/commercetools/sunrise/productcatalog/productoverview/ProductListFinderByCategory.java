package com.commercetools.sunrise.productcatalog.productoverview;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.PriceSelection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.PagedSearchResult;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class ProductListFinderByCategory extends AbstractProductSearchExecutor implements ProductListFinder {

    private final PriceSelection priceSelection;

    @Inject
    protected ProductListFinderByCategory(final SphereClient sphereClient, final HookRunner hookRunner, final PriceSelection priceSelection) {
        super(sphereClient, hookRunner);
        this.priceSelection = priceSelection;
    }

    @Override
    public CompletionStage<PagedSearchResult<ProductProjection>> apply(@Nullable final Category category) {
        return executeRequest(buildRequest(category));
    }

    protected ProductProjectionSearch buildRequest(@Nullable final Category category) {
        // In our case category is filtered via faceted search
        return ProductProjectionSearch.ofCurrent()
                .withMarkingMatchingVariants(false)
                .withPriceSelection(priceSelection);
    }
}
