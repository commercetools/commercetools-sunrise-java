package common.pages;

import common.cms.CmsPage;

public abstract class PageData {
    protected final CmsPage cms;

    public PageData(final CmsPage cms) {
        this.cms = cms;
    }

    protected abstract String additionalTitle();

    public String getTitle() {
        return cms.getOrEmpty("title.base") + additionalTitle();
    }

    public HeaderData getHeader() {
        return new HeaderData(cms);
    }
}
