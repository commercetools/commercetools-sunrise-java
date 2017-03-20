package com.commercetools.sunrise.search.pagination;

import com.commercetools.sunrise.framework.components.controllers.ControllerComponent;
import com.commercetools.sunrise.framework.hooks.application.PageDataReadyHook;
import com.commercetools.sunrise.framework.viewmodels.PageData;
import com.commercetools.sunrise.search.pagination.viewmodels.AbstractEntriesPerPageSelectorViewModelFactory;
import com.commercetools.sunrise.search.pagination.viewmodels.AbstractPaginationViewModelFactory;
import com.commercetools.sunrise.search.pagination.viewmodels.WithPaginationViewModel;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.queries.PagedResult;

import javax.annotation.Nullable;

public abstract class AbstractPaginationControllerComponent extends Base implements ControllerComponent, PageDataReadyHook {

    private final PaginationSettings paginationSettings;
    private final EntriesPerPageFormSettings entriesPerPageFormSettings;

    private final AbstractPaginationViewModelFactory paginationViewModelFactory;
    private final AbstractEntriesPerPageSelectorViewModelFactory entriesPerPageSelectorViewModelFactory;

    protected AbstractPaginationControllerComponent(final PaginationSettings paginationSettings,
                                                    final EntriesPerPageFormSettings entriesPerPageFormSettings,
                                                    final AbstractPaginationViewModelFactory paginationViewModelFactory,
                                                    final AbstractEntriesPerPageSelectorViewModelFactory entriesPerPageSelectorViewModelFactory) {
        this.paginationSettings = paginationSettings;
        this.entriesPerPageFormSettings = entriesPerPageFormSettings;
        this.paginationViewModelFactory = paginationViewModelFactory;
        this.entriesPerPageSelectorViewModelFactory = entriesPerPageSelectorViewModelFactory;
    }

    protected final PaginationSettings getPaginationSettings() {
        return paginationSettings;
    }

    protected final EntriesPerPageFormSettings getEntriesPerPageSettings() {
        return entriesPerPageFormSettings;
    }

    @Nullable
    protected abstract PagedResult<?> getPagedResult();

    @Override
    public void onPageDataReady(final PageData pageData) {
        final PagedResult<?> pagedResult = getPagedResult();
        if (pagedResult != null && pageData.getContent() instanceof WithPaginationViewModel) {
            final WithPaginationViewModel content = (WithPaginationViewModel) pageData.getContent();
            content.setPagination(paginationViewModelFactory.create(pagedResult));
            content.setDisplaySelector(entriesPerPageSelectorViewModelFactory.create(pagedResult));
        }
    }
}
