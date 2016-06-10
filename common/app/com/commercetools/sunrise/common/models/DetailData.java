package com.commercetools.sunrise.common.models;

import io.sphere.sdk.models.Base;

public class DetailData extends Base {
    private String title;
    private String text;
    private String description;

    public DetailData() {
    }

    public DetailData(final String title) {
        this.title = title;
        this.text = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }
}
