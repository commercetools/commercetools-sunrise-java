package com.commercetools.sunrise.productcatalog.products.search.facetedsearch;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.models.products.ProductListFetcherHook;
import com.commercetools.sunrise.models.search.facetedsearch.AbstractFacetedSearchSelectorComponent;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.FacetedSearchExpression;
import io.sphere.sdk.search.PagedSearchResult;
import play.mvc.Http;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public final class ProductFacetedSearchSelectorComponent extends AbstractFacetedSearchSelectorComponent<ProductProjection> implements ControllerComponent, ProductListFetcherHook {

    @Nullable
    private CompletionStage<PagedSearchResult<ProductProjection>> resultStage;

    @Inject
    public ProductFacetedSearchSelectorComponent(final ProductFacetedSearchFormSettingsList settingsList,
                                                 final ProductFacetSelectorListViewModelFactory productFacetSelectorListViewModelFactory) {
        super(settingsList, productFacetSelectorListViewModelFactory);
    }

    @Override
    protected CompletionStage<PagedSearchResult<ProductProjection>> getResultStage() {
        return resultStage;
    }

    @Override
    public CompletionStage<PagedSearchResult<ProductProjection>> on(final ProductProjectionSearch request, final Function<ProductProjectionSearch, CompletionStage<PagedSearchResult<ProductProjection>>> nextComponent) {
        this.resultStage = nextComponent.apply(requestWithFacets(request));
        return resultStage;
    }

    private ProductProjectionSearch requestWithFacets(final ProductProjectionSearch request) {
        final List<FacetedSearchExpression<ProductProjection>> facetedSearchExpressions = getSettings().buildFacetedSearchExpressions(Http.Context.current());
        return facetedSearchExpressions.isEmpty() ? request : request.plusFacetedSearch(facetedSearchExpressions);
    }
}
