package com.commercetools.sunrise.common.models;

import io.sphere.sdk.models.Base;

public class HalLink extends Base {

    private String href;

    public HalLink() {
    }

    public HalLink(final String href) {
        setHref(href);
    }

    public String getHref() {
        return href;
    }

    public void setHref(final String href) {
        this.href = href;
    }
}
