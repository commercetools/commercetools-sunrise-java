package com.commercetools.sunrise.common.pages;

import io.sphere.sdk.models.Base;

public abstract class PageContent extends Base {

    private String title;

    public PageContent() {
    }

    public final String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }
}
