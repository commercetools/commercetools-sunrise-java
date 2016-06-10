package com.commercetools.sunrise.productcatalog.productoverview;

import common.contexts.UserContext;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.PagedSearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import java.util.function.UnaryOperator;

import static common.utils.LogUtils.logProductRequest;
import static common.utils.PriceUtils.createPriceSelection;

public class ProductListFetchSimple implements ProductListFetch<Void> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductListFetchSimple.class);

    @Inject
    private SphereClient sphereClient;
    @Inject
    private UserContext userContext;

    public CompletionStage<PagedSearchResult<ProductProjection>> searchProducts(final Void criteria, final UnaryOperator<ProductProjectionSearch> filter) {
        final ProductProjectionSearch baseRequest = ProductProjectionSearch.ofCurrent()
                .withPriceSelection(createPriceSelection(userContext));
        final ProductProjectionSearch request = filter.apply(baseRequest);
        return sphereClient.execute(request)
                .whenCompleteAsync((result, t) -> logProductRequest(LOGGER, request, result), HttpExecution.defaultContext());
    }
}
