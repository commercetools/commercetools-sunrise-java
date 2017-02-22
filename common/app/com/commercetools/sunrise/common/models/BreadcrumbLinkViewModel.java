package com.commercetools.sunrise.common.models;

import io.sphere.sdk.models.LocalizedString;

public class BreadcrumbLinkViewModel extends ViewModel {

    private LocalizedString text;
    private String url;

    public BreadcrumbLinkViewModel() {
    }

    public LocalizedString getText() {
        return text;
    }

    public void setText(final LocalizedString text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }
}
