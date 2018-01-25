package com.commercetools.sunrise.productcatalog.products.search.pagination;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.models.products.ProductListFetcherHook;
import com.commercetools.sunrise.models.search.pagination.AbstractPaginationControllerComponent;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.PagedSearchResult;
import play.mvc.Http;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public final class ProductPaginationControllerComponent extends AbstractPaginationControllerComponent<PagedSearchResult<ProductProjection>>
        implements ControllerComponent, ProductListFetcherHook {

    @Nullable
    private CompletionStage<PagedSearchResult<ProductProjection>> resultStage;

    @Inject
    public ProductPaginationControllerComponent(final ProductPaginationSettings paginationSettings,
                                                final ProductsPerPageFormSettings entriesPerPageFormSettings,
                                                final ProductPaginationViewModelFactory paginationViewModelFactory,
                                                final ProductsPerPageSelectorViewModelFactory entriesPerPageSelectorViewModelFactory) {
        super(paginationSettings, entriesPerPageFormSettings, paginationViewModelFactory, entriesPerPageSelectorViewModelFactory);
    }

    @Nullable
    @Override
    protected CompletionStage<PagedSearchResult<ProductProjection>> getResultStage() {
        return resultStage;
    }

    @Override
    public CompletionStage<PagedSearchResult<ProductProjection>> on(final ProductProjectionSearch request,
                                                                    final Function<ProductProjectionSearch, CompletionStage<PagedSearchResult<ProductProjection>>> nextComponent) {
        this.resultStage = nextComponent.apply(requestWithPagination(request));
        return resultStage;
    }

    private ProductProjectionSearch requestWithPagination(final ProductProjectionSearch search) {
        final Http.Context context = Http.Context.current();
        final long limit = getEntriesPerPageSettings().getLimit(context);
        final long offset = getPaginationSettings().getOffset(context, limit);
        return search.withOffset(offset).withLimit(limit);
    }
}
