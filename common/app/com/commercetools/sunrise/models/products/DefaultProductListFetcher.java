package com.commercetools.sunrise.models.products;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.products.search.PriceSelection;
import io.sphere.sdk.products.search.ProductProjectionSearch;

import javax.inject.Inject;

public final class DefaultProductListFetcher extends AbstractProductListFetcher {

    private final PriceSelection priceSelection;

    @Inject
    DefaultProductListFetcher(final HookRunner hookRunner, final SphereClient sphereClient,
                              final PriceSelection priceSelection) {
        super(hookRunner, sphereClient);
        this.priceSelection = priceSelection;
    }

    @Override
    protected ProductProjectionSearch buildRequest() {
        return ProductProjectionSearch.ofCurrent().withPriceSelection(priceSelection);
    }
}
