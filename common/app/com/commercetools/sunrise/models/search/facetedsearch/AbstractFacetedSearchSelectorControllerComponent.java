package com.commercetools.sunrise.models.search.facetedsearch;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.core.hooks.application.PageDataHook;
import com.commercetools.sunrise.core.viewmodels.OldPageData;
import com.commercetools.sunrise.models.search.facetedsearch.viewmodels.AbstractFacetSelectorListViewModelFactory;
import com.commercetools.sunrise.models.search.facetedsearch.viewmodels.WithFacetedSearchViewModel;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.search.PagedSearchResult;

import javax.annotation.Nullable;

public abstract class AbstractFacetedSearchSelectorControllerComponent<T> extends Base implements ControllerComponent, PageDataHook {

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
    public void onPageDataReady(final OldPageData oldPageData) {
        final PagedSearchResult<T> pagedSearchResult = getPagedSearchResult();
        if (pagedSearchResult != null && oldPageData.getContent() instanceof WithFacetedSearchViewModel) {
            final WithFacetedSearchViewModel content = (WithFacetedSearchViewModel) oldPageData.getContent();
            content.setFacets(facetSelectorListViewModelFactory.create(pagedSearchResult));
        }
    }
}
