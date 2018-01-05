package com.commercetools.sunrise.models.products;

import com.commercetools.sunrise.core.controllers.AbstractSearchExecutor;
import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.core.hooks.ctpevents.ProductProjectionPagedSearchResultLoadedHook;
import com.commercetools.sunrise.core.hooks.ctprequests.ProductProjectionSearchHook;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.*;
import io.sphere.sdk.search.model.SimpleRangeStats;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class AbstractProductListFetcher extends AbstractSearchExecutor<ProductProjection, ProductProjectionSearch> implements ProductListFetcher {

    protected AbstractProductListFetcher(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    public CompletionStage<PagedSearchResult<ProductProjection>> get() {
        return buildRequest().map(this::executeRequest).orElseGet(() -> completedFuture(emptyPagedSearchResult()));
    }

    @Override
    protected ProductProjectionSearch runSearchHook(final ProductProjectionSearch baseRequest) {
        return ProductProjectionSearchHook.runHook(getHookRunner(), baseRequest);
    }

    @Override
    protected CompletionStage<?> runPagedSearchResultLoadedHook(final PagedSearchResult<ProductProjection> result) {
        return ProductProjectionPagedSearchResultLoadedHook.runHook(getHookRunner(), result);
    }

    protected abstract Optional<ProductProjectionSearch> buildRequest();

    // TODO Replace once implemented in JVM SDK (issue https://github.com/commercetools/commercetools-jvm-sdk/issues/1662)
    private PagedSearchResult<ProductProjection> emptyPagedSearchResult() {
        return new PagedSearchResult<ProductProjection>() {
            @Override
            public Map<String, FacetResult> getFacetsResults() {
                return Collections.emptyMap();
            }

            @Override
            public FacetResult getFacetResult(final String facetResultPath) {
                return null;
            }

            @Override
            public TermFacetResult getFacetResult(final TermFacetExpression<ProductProjection> facetExpression) {
                return null;
            }

            @Override
            public RangeFacetResult getFacetResult(final RangeFacetExpression<ProductProjection> facetExpression) {
                return null;
            }

            @Override
            public FilteredFacetResult getFacetResult(final FilteredFacetExpression<ProductProjection> facetExpression) {
                return null;
            }

            @Override
            public TermFacetResult getFacetResult(final TermFacetedSearchExpression<ProductProjection> facetedSearchExpression) {
                return null;
            }

            @Override
            public RangeFacetResult getFacetResult(final RangeFacetedSearchExpression<ProductProjection> facetedSearchExpression) {
                return null;
            }

            @Override
            public SimpleRangeStats getRangeStatsOfAllRanges(final RangeFacetExpression<ProductProjection> facetExpression) {
                return null;
            }

            @Override
            public SimpleRangeStats getRangeStatsOfAllRanges(final RangeFacetedSearchExpression<ProductProjection> facetedSearchExpression) {
                return null;
            }

            @Override
            public TermFacetResult getTermFacetResult(final String facetResultPath) {
                return null;
            }

            @Override
            public RangeFacetResult getRangeFacetResult(final String facetResultPath) {
                return null;
            }

            @Override
            public FilteredFacetResult getFilteredFacetResult(final String facetResultPath) {
                return null;
            }

            @Override
            public List<ProductProjection> getResults() {
                return Collections.emptyList();
            }

            @Override
            public Long getOffset() {
                return 0L;
            }

            @Override
            public Long getTotal() {
                return 0L;
            }

            @Override
            public Long size() {
                return 0L;
            }

            @Override
            public Long getCount() {
                return 0L;
            }
        };
    }
}
