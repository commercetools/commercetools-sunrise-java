package com.commercetools.sunrise.common.models;

import io.sphere.sdk.models.Base;

public class LinkBean extends Base {
    private String text;
    private String url;
    private boolean selected;

    public LinkBean() {
    }

    public LinkBean(final String text, final String url) {
        this.text = text;
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(final String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(final boolean selected) {
        this.selected = selected;
    }
}
