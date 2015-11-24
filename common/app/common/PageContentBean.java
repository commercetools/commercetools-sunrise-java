package common;

import common.controllers.PageContent;

public abstract class PageContentBean extends PageContent {
    private String additionalTitle;

    public PageContentBean() {
    }

    @Override
    public final String getAdditionalTitle() {
        return additionalTitle;
    }

    public void setAdditionalTitle(final String additionalTitle) {
        this.additionalTitle = additionalTitle;
    }
}
