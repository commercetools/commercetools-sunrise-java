package com.commercetools.sunrise.models.products;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.products.search.PriceSelection;
import io.sphere.sdk.products.search.ProductProjectionSearch;

import javax.inject.Inject;
import java.util.Optional;

public class DefaultProductListFetcher extends AbstractProductListFetcher {

    private final PriceSelection priceSelection;

    @Inject
    protected DefaultProductListFetcher(final SphereClient sphereClient, final HookRunner hookRunner, final PriceSelection priceSelection) {
        super(sphereClient, hookRunner);
        this.priceSelection = priceSelection;
    }

    @Override
    public Optional<ProductProjectionSearch> defaultRequest() {
        return Optional.of(ProductProjectionSearch.ofCurrent()
                .withMarkingMatchingVariants(false)
                .withPriceSelection(priceSelection));
    }
}
