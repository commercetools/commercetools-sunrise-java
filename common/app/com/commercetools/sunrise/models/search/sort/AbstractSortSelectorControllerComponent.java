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

public abstract class AbstractSortSelectorControllerComponent<T> extends Base implements ControllerComponent, PageDataHook {

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
    protected abstract PagedResult<T> getPagedResult();

    @Override
    public CompletionStage<PageData> onPageDataReady(final PageData pageData) {
        final PagedResult<T> pagedResult = getPagedResult();
        if (pagedResult != null) {
            pageData.put("sortSelector", sortSelectorViewModelFactory.create(pagedResult));
        }
        return completedFuture(pageData);
    }
}
