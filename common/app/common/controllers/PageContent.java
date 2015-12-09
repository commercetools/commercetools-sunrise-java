package common.controllers;

import io.sphere.sdk.models.Base;

public abstract class PageContent extends Base {
    private String additionalTitle;

    public PageContent() {
    }

    public final String getAdditionalTitle() {
        return additionalTitle;
    }

    public void setAdditionalTitle(final String additionalTitle) {
        this.additionalTitle = additionalTitle;
    }
}
