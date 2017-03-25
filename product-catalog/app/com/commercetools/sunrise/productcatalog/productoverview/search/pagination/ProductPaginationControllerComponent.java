package com.commercetools.sunrise.productcatalog.productoverview.search.pagination;

import com.commercetools.sunrise.framework.components.controllers.ControllerComponent;
import com.commercetools.sunrise.framework.hooks.ctpevents.ProductProjectionPagedSearchResultLoadedHook;
import com.commercetools.sunrise.framework.hooks.ctprequests.ProductProjectionSearchHook;
import com.commercetools.sunrise.search.pagination.AbstractPaginationControllerComponent;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.queries.PagedResult;
import io.sphere.sdk.search.PagedSearchResult;
import play.mvc.Http;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public final class ProductPaginationControllerComponent extends AbstractPaginationControllerComponent
        implements ControllerComponent, ProductProjectionSearchHook, ProductProjectionPagedSearchResultLoadedHook {

    @Nullable
    private PagedResult<?> pagedResult;

    @Inject
    public ProductPaginationControllerComponent(final ProductPaginationSettings paginationSettings,
                                                final ProductsPerPageFormSettings entriesPerPageFormSettings,
                                                final ProductPaginationViewModelFactory paginationViewModelFactory,
                                                final ProductsPerPageSelectorViewModelFactory entriesPerPageSelectorViewModelFactory) {
        super(paginationSettings, entriesPerPageFormSettings, paginationViewModelFactory, entriesPerPageSelectorViewModelFactory);
    }

    @Nullable
    @Override
    protected PagedResult<?> getPagedResult() {
        return pagedResult;
    }

    @Override
    public ProductProjectionSearch onProductProjectionSearch(final ProductProjectionSearch search) {
        final Http.Context context = Http.Context.current();
        final long limit = getEntriesPerPageSettings().getLimit(context);
        final long offset = getPaginationSettings().getOffset(context, limit);
        return search
                .withOffset(offset)
                .withLimit(limit);
    }

    @Override
    public CompletionStage<?> onProductProjectionPagedSearchResultLoaded(final PagedSearchResult<ProductProjection> pagedSearchResult) {
        this.pagedResult = pagedSearchResult;
        return completedFuture(null);
    }
}
