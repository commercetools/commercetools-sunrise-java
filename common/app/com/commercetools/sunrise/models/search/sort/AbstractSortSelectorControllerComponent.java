package com.commercetools.sunrise.models.search.sort;

import com.commercetools.sunrise.core.components.controllers.ControllerComponent;
import com.commercetools.sunrise.core.hooks.application.PageDataReadyHook;
import com.commercetools.sunrise.core.viewmodels.PageData;
import com.commercetools.sunrise.models.search.sort.viewmodels.AbstractSortSelectorViewModelFactory;
import com.commercetools.sunrise.models.search.sort.viewmodels.WithSortSelectorViewModel;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.queries.PagedResult;

import javax.annotation.Nullable;

public abstract class AbstractSortSelectorControllerComponent<T> extends Base implements ControllerComponent, PageDataReadyHook {

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
    public void onPageDataReady(final PageData pageData) {
        final PagedResult<T> pagedResult = getPagedResult();
        if (pagedResult != null && pageData.getContent() instanceof WithSortSelectorViewModel) {
            final WithSortSelectorViewModel content = (WithSortSelectorViewModel) pageData.getContent();
            content.setSortSelector(sortSelectorViewModelFactory.create(pagedResult));
        }
    }
}
