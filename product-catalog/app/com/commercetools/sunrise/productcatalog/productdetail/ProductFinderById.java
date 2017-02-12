package com.commercetools.sunrise.productcatalog.productdetail;

import com.commercetools.sunrise.hooks.HookContext;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.PriceSelection;
import io.sphere.sdk.products.search.ProductProjectionSearch;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public class ProductFinderById extends AbstractProductSearchExecutor implements ProductFinder {

    private final PriceSelection priceSelection;

    @Inject
    protected ProductFinderById(final SphereClient sphereClient, final HookContext hookContext, final PriceSelection priceSelection) {
        super(sphereClient, hookContext);
        this.priceSelection = priceSelection;
    }

    @Override
    public CompletionStage<Optional<ProductProjection>> apply(final String id) {
        return executeRequest(buildRequest(id));
    }

    protected ProductProjectionSearch buildRequest(final String id) {
        return ProductProjectionSearch.ofCurrent()
                .withQueryFilters(m -> m.id().is(id))
                .withPriceSelection(priceSelection);
    }
}