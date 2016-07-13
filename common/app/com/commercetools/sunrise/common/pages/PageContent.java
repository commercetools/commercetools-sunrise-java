package com.commercetools.sunrise.common.pages;

import com.commercetools.sunrise.common.models.ModelBean;

public abstract class PageContent extends ModelBean {

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
