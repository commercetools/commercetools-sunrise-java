package com.commercetools.sunrise.models.search.searchbox;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.core.hooks.application.PageDataHook;
import com.commercetools.sunrise.core.viewmodels.PageData;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.models.LocalizedStringEntry;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class AbstractSearchBoxControllerComponent extends Base implements ControllerComponent, PageDataHook {

    private final SearchBoxSettings searchBoxSettings;

    protected AbstractSearchBoxControllerComponent(final SearchBoxSettings searchBoxSettings) {
        this.searchBoxSettings = searchBoxSettings;
    }

    protected final SearchBoxSettings getSearchBoxSettings() {
        return searchBoxSettings;
    }

    protected abstract Optional<LocalizedStringEntry> getSearchText();

    @Override
    public CompletionStage<PageData> onPageDataReady(final PageData pageData) {
        return completedFuture(getSearchText()
                .map(searchText -> pageData.put("searchTerm", searchText.getValue()))
                .orElse(pageData));
    }
}
