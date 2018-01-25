package com.commercetools.sunrise.models.search.pagination;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.core.hooks.application.PageDataHook;
import com.commercetools.sunrise.core.viewmodels.PageData;
import com.commercetools.sunrise.models.search.pagination.viewmodels.AbstractEntriesPerPageSelectorViewModelFactory;
import com.commercetools.sunrise.models.search.pagination.viewmodels.AbstractPaginationViewModelFactory;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.queries.PagedResult;
import play.mvc.Http;

import javax.annotation.Nullable;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class AbstractPaginationControllerComponent<P extends PagedResult<?>> extends Base implements ControllerComponent, PageDataHook {

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
    protected abstract CompletionStage<P> getResultStage();

    @Override
    public CompletionStage<PageData> onPageDataReady(final PageData pageData) {
        final CompletionStage<P> resultStage = getResultStage();
        if (resultStage != null) {
            final Long currentPage = paginationSettings.getSelectedValueOrDefault(Http.Context.current());
            return resultStage.thenApply(result ->
                    pageData.put("pagination", paginationViewModelFactory.create(result, currentPage))
                            .put("displaySelector", entriesPerPageSelectorViewModelFactory.create(result)));
        }
        return completedFuture(pageData);
    }
}
