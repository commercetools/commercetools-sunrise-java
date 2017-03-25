package com.commercetools.sunrise.productcatalog.productoverview.search.sort;

import com.commercetools.sunrise.framework.components.controllers.ControllerComponent;
import com.commercetools.sunrise.framework.hooks.ctpevents.ProductProjectionPagedSearchResultLoadedHook;
import com.commercetools.sunrise.framework.hooks.ctprequests.ProductProjectionSearchHook;
import com.commercetools.sunrise.search.sort.AbstractSortSelectorControllerComponent;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.queries.PagedResult;
import io.sphere.sdk.search.PagedSearchResult;
import io.sphere.sdk.search.SortExpression;
import play.mvc.Http;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public final class ProductSearchSortSelectorControllerComponent extends AbstractSortSelectorControllerComponent<ProductProjection>
        implements ControllerComponent, ProductProjectionSearchHook, ProductProjectionPagedSearchResultLoadedHook {

    @Nullable
    private PagedResult<ProductProjection> pagedResult;

    @Inject
    public ProductSearchSortSelectorControllerComponent(final ProductSortFormSettings productSortFormSettings,
                                                        final ProductSearchSortSelectorViewModelFactory sortSelectorViewModelFactory) {
        super(productSortFormSettings, sortSelectorViewModelFactory);
    }

    @Nullable
    @Override
    protected PagedResult<ProductProjection> getPagedResult() {
        return pagedResult;
    }

    @Override
    public ProductProjectionSearch onProductProjectionSearch(final ProductProjectionSearch search) {
        final List<SortExpression<ProductProjection>> sortExpressions = getSortFormSettings().buildSearchExpressions(Http.Context.current());
        if (!sortExpressions.isEmpty()) {
            return search.plusSort(sortExpressions);
        } else {
            return search;
        }
    }

    @Override
    public CompletionStage<?> onProductProjectionPagedSearchResultLoaded(final PagedSearchResult<ProductProjection> pagedSearchResult) {
        this.pagedResult = pagedSearchResult;
        return completedFuture(null);
    }
}
