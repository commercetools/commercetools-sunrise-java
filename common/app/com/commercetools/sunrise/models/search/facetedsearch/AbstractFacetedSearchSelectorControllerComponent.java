package com.commercetools.sunrise.models.search.facetedsearch;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.core.hooks.application.PageDataHook;
import com.commercetools.sunrise.core.viewmodels.PageData;
import com.commercetools.sunrise.models.search.facetedsearch.viewmodels.AbstractFacetSelectorListViewModelFactory;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.search.PagedSearchResult;

import javax.annotation.Nullable;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

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
    public CompletionStage<PageData> onPageDataReady(final PageData pageData) {
        final PagedSearchResult<T> pagedSearchResult = getPagedSearchResult();
        if (pagedSearchResult != null) {
            pageData.put("facets", facetSelectorListViewModelFactory.create(pagedSearchResult));
        }
        return completedFuture(pageData);
    }
}
