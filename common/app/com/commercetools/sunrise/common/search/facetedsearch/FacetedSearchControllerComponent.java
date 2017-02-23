package com.commercetools.sunrise.common.search.facetedsearch;

import com.commercetools.sunrise.common.pages.PageData;
import com.commercetools.sunrise.framework.components.ControllerComponent;
import com.commercetools.sunrise.framework.hooks.consumers.PageDataReadyHook;
import com.commercetools.sunrise.framework.hooks.events.CategoryLoadedHook;
import com.commercetools.sunrise.framework.hooks.events.ProductProjectionPagedSearchResultLoadedHook;
import com.commercetools.sunrise.framework.hooks.requests.ProductProjectionSearchHook;
import com.commercetools.sunrise.framework.template.i18n.I18nIdentifierResolver;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.facets.Facet;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.PagedSearchResult;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.stream.Collectors.toList;

public final class FacetedSearchControllerComponent implements ControllerComponent, PageDataReadyHook, ProductProjectionSearchHook, ProductProjectionPagedSearchResultLoadedHook, CategoryLoadedHook {

    private final I18nIdentifierResolver i18nIdentifierResolver;
    private final FacetedSearchSelectorListFactory facetedSearchSelectorListFactory;

    private List<Category> selectedCategories = emptyList();
    private List<FacetedSearchSelector> facetedSearchSelectorList = emptyList();
    private List<FacetSelectorViewModel> facetViewModels = emptyList();

    @Inject
    public FacetedSearchControllerComponent(final I18nIdentifierResolver i18nIdentifierResolver, final FacetedSearchSelectorListFactory facetedSearchSelectorListFactory) {
        this.i18nIdentifierResolver = i18nIdentifierResolver;
        this.facetedSearchSelectorListFactory = facetedSearchSelectorListFactory;
    }

    @Override
    public CompletionStage<?> onCategoryLoaded(final Category category) {
        this.selectedCategories = singletonList(category);
        return completedFuture(null);
    }

    @Override
    public ProductProjectionSearch onProductProjectionSearch(final ProductProjectionSearch search) {
        this.facetedSearchSelectorList = facetedSearchSelectorListFactory.create(selectedCategories);
        return search.plusFacetedSearch(facetedSearchSelectorList.stream()
                .map(FacetedSearchSelector::getFacetedSearchExpression)
                .collect(toList()));
    }

    @Override
    public CompletionStage<?> onProductProjectionPagedSearchResultLoaded(final PagedSearchResult<ProductProjection> pagedSearchResult) {
        facetViewModels = facetedSearchSelectorList.stream()
                .sorted(Comparator.comparingDouble(FacetedSearchSelector::getPosition))
                .map(facetedSearchSelector -> createFacetSelectorViewModel(facetedSearchSelector, pagedSearchResult))
                .collect(toList());
        return completedFuture(null);
    }

    @Override
    public void onPageDataReady(final PageData pageData) {
        if (pageData.getContent() instanceof WithFacetedSearchViewModel) {
            final WithFacetedSearchViewModel content = (WithFacetedSearchViewModel) pageData.getContent();
            content.setFacets(createFacetSelectorList(facetViewModels));
        }
    }

    private FacetSelectorViewModel createFacetSelectorViewModel(final FacetedSearchSelector facetedSearchSelector, final PagedSearchResult<ProductProjection> searchResult) {
        final FacetSelectorViewModel viewModel = new FacetSelectorViewModel();
        final Facet<ProductProjection> facet = facetedSearchSelector.getFacet(searchResult);
        if (facet.getLabel() != null) {
            viewModel.setFacet(facet.withLabel(i18nIdentifierResolver.resolveOrKey(facet.getLabel())));
        } else {
            viewModel.setFacet(facet);
        }
        return viewModel;
    }

    private FacetSelectorListViewModel createFacetSelectorList(final List<FacetSelectorViewModel> facetSelectorViewModels) {
        final FacetSelectorListViewModel viewModel = new FacetSelectorListViewModel();
        viewModel.setList(facetSelectorViewModels);
        return viewModel;
    }
}
