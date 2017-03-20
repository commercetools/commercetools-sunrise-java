package com.commercetools.sunrise.search.searchbox;

import com.commercetools.sunrise.framework.components.controllers.ControllerComponent;
import com.commercetools.sunrise.framework.hooks.application.PageDataReadyHook;
import com.commercetools.sunrise.framework.viewmodels.PageData;
import com.commercetools.sunrise.search.searchbox.viewmodels.WithSearchBoxViewModel;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.models.LocalizedStringEntry;

public abstract class AbstractSearchBoxControllerComponent extends Base implements ControllerComponent, PageDataReadyHook {

    private final SearchBoxSettings settings;

    protected AbstractSearchBoxControllerComponent(final SearchBoxSettings settings) {
        this.settings = settings;
    }

    protected final SearchBoxSettings getSettings() {
        return settings;
    }

    protected abstract LocalizedStringEntry getSearchText();

    @Override
    public void onPageDataReady(final PageData pageData) {
        final LocalizedStringEntry searchText = getSearchText();
        if (!searchText.getValue().isEmpty() && pageData.getContent() instanceof WithSearchBoxViewModel) {
            final WithSearchBoxViewModel content = (WithSearchBoxViewModel) pageData.getContent();
            content.setSearchTerm(searchText.getValue());
        }
    }
}
