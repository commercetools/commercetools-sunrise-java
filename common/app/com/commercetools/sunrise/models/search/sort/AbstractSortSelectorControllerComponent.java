package com.commercetools.sunrise.models.search.sort;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.core.hooks.application.PageDataHook;
import com.commercetools.sunrise.core.viewmodels.PageData;
import com.commercetools.sunrise.models.search.sort.viewmodels.AbstractSortSelectorViewModelFactory;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.queries.PagedResult;

import javax.annotation.Nullable;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class AbstractSortSelectorControllerComponent<T, P extends PagedResult<T>> extends Base implements ControllerComponent, PageDataHook {

    private final SortFormSettings<T> sortFormSettings;
    private final AbstractSortSelectorViewModelFactory<T> sortSelectorViewModelFactory;

    protected AbstractSortSelectorControllerComponent(final SortFormSettings<T> sortFormSettings,
                                                      final AbstractSortSelectorViewModelFactory<T> sortSelectorViewModelFactory) {
        this.sortFormSettings = sortFormSettings;
        this.sortSelectorViewModelFactory = sortSelectorViewModelFactory;
    }

    protected final SortFormSettings<T> getSortFormSettings() {
        return sortFormSettings;
    }

    @Nullable
    protected abstract CompletionStage<P> getResultStage();

    @Override
    public CompletionStage<PageData> onPageDataReady(final PageData pageData) {
        final CompletionStage<P> pagedResult = getResultStage();
        if (pagedResult != null) {
            return pagedResult.thenApply(result ->
                    pageData.put("sortSelector", sortSelectorViewModelFactory.create(result)));
        }
        return completedFuture(pageData);
    }
}
