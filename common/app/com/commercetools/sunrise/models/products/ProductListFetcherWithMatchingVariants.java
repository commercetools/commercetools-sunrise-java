package com.commercetools.sunrise.models.products;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.products.search.PriceSelection;
import io.sphere.sdk.products.search.ProductProjectionSearch;

import javax.inject.Inject;
import java.util.Optional;

public final class ProductListFetcherWithMatchingVariants extends DefaultProductListFetcher {

    @Inject
    ProductListFetcherWithMatchingVariants(final SphereClient sphereClient, final HookRunner hookRunner,
                                           final PriceSelection priceSelection) {
        super(sphereClient, hookRunner, priceSelection);
    }

    @Override
    protected Optional<ProductProjectionSearch> buildRequest() {
        return super.buildRequest().map(request -> request.withMarkingMatchingVariants(true));
    }
}
