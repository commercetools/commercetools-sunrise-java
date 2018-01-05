package com.commercetools.sunrise.models.products;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.products.queries.ProductProjectionQuery;
import io.sphere.sdk.products.search.PriceSelection;

import javax.inject.Inject;
import java.util.Optional;

public class ProductFetcherById extends AbstractProductFetcher implements ProductFetcher {

    private final PriceSelection priceSelection;

    @Inject
    protected ProductFetcherById(final SphereClient sphereClient, final HookRunner hookRunner, final PriceSelection priceSelection) {
        super(sphereClient, hookRunner);
        this.priceSelection = priceSelection;
    }

    @Override
    protected Optional<ProductProjectionQuery> buildRequest(final String id) {
        return Optional.of(ProductProjectionQuery.ofCurrent()
                .withPredicates(m -> m.id().is(id))
                .withPriceSelection(priceSelection));
    }
}
