package com.commercetools.sunrise.models.search.facetedsearch;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.core.hooks.application.PageDataHook;
import com.commercetools.sunrise.core.viewmodels.PageData;
import com.commercetools.sunrise.models.search.facetedsearch.viewmodels.AbstractFacetSelectorListViewModelFactory;
import com.commercetools.sunrise.models.search.facetedsearch.viewmodels.FacetSelectorListViewModel;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.search.PagedSearchResult;
import play.libs.concurrent.HttpExecution;

import javax.annotation.Nullable;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class AbstractFacetedSearchSelectorComponent<T> extends Base implements ControllerComponent, PageDataHook {

    private final FacetedSearchFormSettingsList<T> settings;
    private final AbstractFacetSelectorListViewModelFactory<T> facetSelectorListViewModelFactory;

    protected AbstractFacetedSearchSelectorComponent(final FacetedSearchFormSettingsList<T> settings,
                                                     final AbstractFacetSelectorListViewModelFactory<T> facetSelectorListViewModelFactory) {
        this.settings = settings;
        this.facetSelectorListViewModelFactory = facetSelectorListViewModelFactory;
    }

    protected final FacetedSearchFormSettingsList<T> getSettings() {
        return settings;
    }

    @Nullable
    protected abstract CompletionStage<PagedSearchResult<T>> getResultStage();

    @Override
    public CompletionStage<PageData> onPageDataReady(final PageData pageData) {
        final CompletionStage<PagedSearchResult<T>> pagedSearchResult = getResultStage();
        if (pagedSearchResult != null) {
            return pagedSearchResult
                    .thenApplyAsync(result -> {
                        final FacetSelectorListViewModel viewModel = facetSelectorListViewModelFactory.create(result);
                        return pageData.put("facets", viewModel);
                    }, HttpExecution.defaultContext());
        }
        return completedFuture(pageData);
    }
}
