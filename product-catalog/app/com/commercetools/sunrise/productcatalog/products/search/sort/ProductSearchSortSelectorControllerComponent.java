package com.commercetools.sunrise.productcatalog.products.search.sort;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.models.products.ProductListFetcherHook;
import com.commercetools.sunrise.models.search.sort.AbstractSortSelectorControllerComponent;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.PagedSearchResult;
import io.sphere.sdk.search.SortExpression;
import play.mvc.Http;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public final class ProductSearchSortSelectorControllerComponent extends AbstractSortSelectorControllerComponent<ProductProjection, PagedSearchResult<ProductProjection>> implements ControllerComponent, ProductListFetcherHook {

    @Nullable
    private CompletionStage<PagedSearchResult<ProductProjection>> resultStage;

    @Inject
    public ProductSearchSortSelectorControllerComponent(final ProductSortFormSettings productSortFormSettings,
                                                        final ProductSearchSortSelectorViewModelFactory sortSelectorViewModelFactory) {
        super(productSortFormSettings, sortSelectorViewModelFactory);
    }

    @Nullable
    @Override
    protected CompletionStage<PagedSearchResult<ProductProjection>> getResultStage() {
        return resultStage;
    }

    @Override
    public CompletionStage<PagedSearchResult<ProductProjection>> on(final ProductProjectionSearch request, final Function<ProductProjectionSearch, CompletionStage<PagedSearchResult<ProductProjection>>> nextComponent) {
        resultStage = nextComponent.apply(requestWithSort(request));
        return resultStage;
    }

    private ProductProjectionSearch requestWithSort(final ProductProjectionSearch search) {
        final List<SortExpression<ProductProjection>> sortExpressions = getSortFormSettings().buildSearchExpressions(Http.Context.current());
        return sortExpressions.isEmpty() ? search : search.plusSort(sortExpressions);
    }
}
