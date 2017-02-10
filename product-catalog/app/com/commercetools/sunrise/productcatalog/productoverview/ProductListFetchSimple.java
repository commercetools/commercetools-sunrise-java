package com.commercetools.sunrise.productcatalog.productoverview;

import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.PriceSelection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.PagedSearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import java.util.function.UnaryOperator;

import static com.commercetools.sunrise.common.utils.LogUtils.logProductRequest;

public class ProductListFetchSimple implements ProductListFetch<Void> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductListFetchSimple.class);

    @Inject
    private SphereClient sphereClient;
    @Inject
    private PriceSelection priceSelection;

    public CompletionStage<PagedSearchResult<ProductProjection>> searchProducts(final Void criteria, final UnaryOperator<ProductProjectionSearch> filter) {
        final ProductProjectionSearch baseRequest = ProductProjectionSearch.ofCurrent()
                .withPriceSelection(priceSelection);
        final ProductProjectionSearch request = filter.apply(baseRequest);
        return sphereClient.execute(request)
                .whenCompleteAsync((result, t) -> logProductRequest(LOGGER, request, result), HttpExecution.defaultContext());
    }
}
