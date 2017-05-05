package com.commercetools.sunrise.search.searchbox;

import com.commercetools.sunrise.framework.components.controllers.ControllerComponent;
import com.commercetools.sunrise.framework.hooks.application.PageDataReadyHook;
import com.commercetools.sunrise.framework.viewmodels.PageData;
import com.commercetools.sunrise.search.searchbox.viewmodels.WithSearchBoxViewModel;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.models.LocalizedStringEntry;

import java.util.Optional;

public abstract class AbstractSearchBoxControllerComponent extends Base implements ControllerComponent, PageDataReadyHook {

    private final SearchBoxSettings searchBoxSettings;

    protected AbstractSearchBoxControllerComponent(final SearchBoxSettings searchBoxSettings) {
        this.searchBoxSettings = searchBoxSettings;
    }

    protected final SearchBoxSettings getSearchBoxSettings() {
        return searchBoxSettings;
    }

    protected abstract Optional<LocalizedStringEntry> getSearchText();

    @Override
    public void onPageDataReady(final PageData pageData) {
        final Optional<LocalizedStringEntry> searchText = getSearchText();
        if (searchText.isPresent() && pageData.getContent() instanceof WithSearchBoxViewModel) {
            final WithSearchBoxViewModel content = (WithSearchBoxViewModel) pageData.getContent();
            content.setSearchTerm(searchText.get().getValue());
        }
    }
}
