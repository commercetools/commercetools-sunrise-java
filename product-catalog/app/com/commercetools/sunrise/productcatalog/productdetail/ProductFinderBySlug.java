package com.commercetools.sunrise.productcatalog.productdetail;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.PriceSelection;
import io.sphere.sdk.products.search.ProductProjectionSearch;

import javax.inject.Inject;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public class ProductFinderBySlug extends AbstractSingleProductSearchExecutor implements ProductFinder {

    private final Locale locale;
    private final PriceSelection priceSelection;

    @Inject
    protected ProductFinderBySlug(final SphereClient sphereClient, final HookRunner hookRunner, final Locale locale,
                                  final PriceSelection priceSelection) {
        super(sphereClient, hookRunner);
        this.locale = locale;
        this.priceSelection = priceSelection;
    }

    @Override
    public CompletionStage<Optional<ProductProjection>> apply(final String slug) {
        return executeRequest(buildRequest(slug));
    }

    protected ProductProjectionSearch buildRequest(final String slug) {
        return ProductProjectionSearch.ofCurrent()
                .withQueryFilters(m -> m.slug().locale(locale).is(slug))
                .withPriceSelection(priceSelection);
    }
}
