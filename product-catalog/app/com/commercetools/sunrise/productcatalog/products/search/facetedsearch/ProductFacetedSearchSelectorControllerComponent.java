package com.commercetools.sunrise.productcatalog.products.search.facetedsearch;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.core.hooks.ctpevents.ProductProjectionPagedSearchResultLoadedHook;
import com.commercetools.sunrise.core.hooks.ctprequests.ProductProjectionSearchHook;
import com.commercetools.sunrise.models.search.facetedsearch.AbstractFacetedSearchSelectorControllerComponent;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.FacetedSearchExpression;
import io.sphere.sdk.search.PagedSearchResult;
import play.mvc.Http;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public final class ProductFacetedSearchSelectorControllerComponent extends AbstractFacetedSearchSelectorControllerComponent<ProductProjection>
        implements ControllerComponent, ProductProjectionSearchHook, ProductProjectionPagedSearchResultLoadedHook {

    @Nullable
    private PagedSearchResult<ProductProjection> result;

    @Inject
    public ProductFacetedSearchSelectorControllerComponent(final ProductFacetedSearchFormSettingsList settingsList,
                                                           final ProductFacetSelectorListViewModelFactory productFacetSelectorListViewModelFactory) {
        super(settingsList, productFacetSelectorListViewModelFactory);
    }

    @Nullable
    @Override
    protected PagedSearchResult<ProductProjection> getResult() {
        return result;
    }

    @Override
    public CompletionStage<ProductProjectionSearch> onProductProjectionSearch(final ProductProjectionSearch search) {
        final List<FacetedSearchExpression<ProductProjection>> facetedSearchExpressions = getSettings().buildFacetedSearchExpressions(Http.Context.current());
        if (!facetedSearchExpressions.isEmpty()) {
            return completedFuture(search.plusFacetedSearch(facetedSearchExpressions));
        } else {
            return completedFuture(search);
        }
    }

    @Override
    public void onProductProjectionPagedSearchResultLoaded(final PagedSearchResult<ProductProjection> pagedSearchResult) {
        result = pagedSearchResult;
    }
}
