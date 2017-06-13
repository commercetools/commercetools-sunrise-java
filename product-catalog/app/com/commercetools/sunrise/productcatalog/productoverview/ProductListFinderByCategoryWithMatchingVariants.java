package com.commercetools.sunrise.productcatalog.productoverview;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.products.search.PriceSelection;
import io.sphere.sdk.products.search.ProductProjectionSearch;

import javax.annotation.Nullable;
import javax.inject.Inject;

public final class ProductListFinderByCategoryWithMatchingVariants extends ProductListFinderByCategory {

    @Inject
    ProductListFinderByCategoryWithMatchingVariants(final SphereClient sphereClient, final HookRunner hookRunner,
                                                    final PriceSelection priceSelection) {
        super(sphereClient, hookRunner, priceSelection);
    }

    @Override
    protected ProductProjectionSearch buildRequest(@Nullable final Category category) {
        return super.buildRequest(category)
                .withMarkingMatchingVariants(true);
    }
}
