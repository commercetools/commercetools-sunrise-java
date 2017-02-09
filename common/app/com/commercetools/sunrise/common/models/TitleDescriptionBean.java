package com.commercetools.sunrise.common.models;

import io.sphere.sdk.models.LocalizedString;

public abstract class TitleDescriptionBean extends ViewModel {

    private LocalizedString title;
    private LocalizedString description;

    public TitleDescriptionBean() {
    }

    public LocalizedString getTitle() {
        return title;
    }

    public void setTitle(final LocalizedString title) {
        this.title = title;
    }

    public LocalizedString getDescription() {
        return description;
    }

    public void setDescription(final LocalizedString description) {
        this.description = description;
    }
}
