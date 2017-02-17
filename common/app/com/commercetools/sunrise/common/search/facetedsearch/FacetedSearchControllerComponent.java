package com.commercetools.sunrise.common.search.facetedsearch;

import com.commercetools.sunrise.common.pages.PageData;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifierFactory;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import com.commercetools.sunrise.framework.ControllerComponent;
import com.commercetools.sunrise.hooks.consumers.PageDataReadyHook;
import com.commercetools.sunrise.hooks.events.CategoryLoadedHook;
import com.commercetools.sunrise.hooks.events.ProductProjectionPagedSearchResultLoadedHook;
import com.commercetools.sunrise.hooks.requests.ProductProjectionSearchHook;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.facets.Facet;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.PagedSearchResult;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletionStage;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.stream.Collectors.toList;

public final class FacetedSearchControllerComponent implements ControllerComponent, PageDataReadyHook, ProductProjectionSearchHook, ProductProjectionPagedSearchResultLoadedHook, CategoryLoadedHook {

    private final Locale locale;
    private final I18nResolver i18nResolver;
    private final I18nIdentifierFactory i18nIdentifierFactory;
    private final FacetedSearchSelectorListFactory facetedSearchSelectorListFactory;

    private List<Category> selectedCategories = emptyList();
    private List<FacetedSearchSelector> facetedSearchSelectorList = emptyList();
    private List<FacetSelectorBean> facetBeans = emptyList();

    @Inject
    public FacetedSearchControllerComponent(final Locale locale, final I18nResolver i18nResolver, final I18nIdentifierFactory i18nIdentifierFactory,
                                            final FacetedSearchSelectorListFactory facetedSearchSelectorListFactory) {
        this.locale = locale;
        this.i18nResolver = i18nResolver;
        this.i18nIdentifierFactory = i18nIdentifierFactory;
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
        facetBeans = facetedSearchSelectorList.stream()
                .sorted(Comparator.comparingDouble(FacetedSearchSelector::getPosition))
                .map(facetedSearchSelector -> createFacetSelectorBean(facetedSearchSelector, pagedSearchResult))
                .collect(toList());
        return completedFuture(null);
    }

    @Override
    public void onPageDataReady(final PageData pageData) {
        if (pageData.getContent() instanceof WithFacetedSearchViewModel) {
            final WithFacetedSearchViewModel content = (WithFacetedSearchViewModel) pageData.getContent();
            content.setFacets(createFacetSelectorList(facetBeans));
        }
    }

    private FacetSelectorBean createFacetSelectorBean(final FacetedSearchSelector facetedSearchSelector, final PagedSearchResult<ProductProjection> searchResult) {
        final FacetSelectorBean bean = new FacetSelectorBean();
        final Facet<ProductProjection> facet = facetedSearchSelector.getFacet(searchResult);
        if (facet.getLabel() != null) {
            final I18nIdentifier i18nIdentifier = i18nIdentifierFactory.create(facet.getLabel());
            final String label = i18nResolver.getOrKey(singletonList(locale), i18nIdentifier);
            bean.setFacet(facet.withLabel(label));
        } else {
            bean.setFacet(facet);
        }
        return bean;
    }

    private FacetSelectorListBean createFacetSelectorList(final List<FacetSelectorBean> facetSelectorBeans) {
        final FacetSelectorListBean bean = new FacetSelectorListBean();
        bean.setList(facetSelectorBeans);
        return bean;
    }
}
