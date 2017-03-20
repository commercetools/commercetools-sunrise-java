package com.commercetools.sunrise.search.facetedsearch;

import com.commercetools.sunrise.framework.components.controllers.ControllerComponent;
import com.commercetools.sunrise.framework.hooks.application.PageDataReadyHook;
import com.commercetools.sunrise.framework.viewmodels.PageData;
import com.commercetools.sunrise.search.facetedsearch.viewmodels.AbstractFacetSelectorListViewModelFactory;
import com.commercetools.sunrise.search.facetedsearch.viewmodels.WithFacetedSearchViewModel;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.search.PagedSearchResult;

import javax.annotation.Nullable;

public abstract class AbstractFacetedSearchSelectorControllerComponent<T> extends Base implements ControllerComponent, PageDataReadyHook {

    private final FacetedSearchFormSettingsList<T> settings;
    private final AbstractFacetSelectorListViewModelFactory<T> facetSelectorListViewModelFactory;

    protected AbstractFacetedSearchSelectorControllerComponent(final FacetedSearchFormSettingsList<T> settings,
                                                               final AbstractFacetSelectorListViewModelFactory<T> facetSelectorListViewModelFactory) {
        this.settings = settings;
        this.facetSelectorListViewModelFactory = facetSelectorListViewModelFactory;
    }

    protected final FacetedSearchFormSettingsList<T> getSettings() {
        return settings;
    }

    @Nullable
    protected abstract PagedSearchResult<T> getPagedSearchResult();

    @Override
    public void onPageDataReady(final PageData pageData) {
        final PagedSearchResult<T> pagedSearchResult = getPagedSearchResult();
        if (pagedSearchResult != null && pageData.getContent() instanceof WithFacetedSearchViewModel) {
            final WithFacetedSearchViewModel content = (WithFacetedSearchViewModel) pageData.getContent();
            content.setFacets(facetSelectorListViewModelFactory.create(pagedSearchResult));
        }
    }
}
