package com.commercetools.sunrise.models.search.searchbox;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.core.hooks.application.PageDataReadyHook;
import com.commercetools.sunrise.core.viewmodels.OldPageData;
import com.commercetools.sunrise.models.search.searchbox.viewmodels.WithSearchBoxViewModel;
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
    public void onPageDataReady(final OldPageData oldPageData) {
        final Optional<LocalizedStringEntry> searchText = getSearchText();
        if (searchText.isPresent() && oldPageData.getContent() instanceof WithSearchBoxViewModel) {
            final WithSearchBoxViewModel content = (WithSearchBoxViewModel) oldPageData.getContent();
            content.setSearchTerm(searchText.get().getValue());
        }
    }
}
