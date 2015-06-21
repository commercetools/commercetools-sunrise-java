package common.pages;

import common.cms.CmsPage;

public abstract class PageContent {
    protected final CmsPage cms;

    public PageContent(final CmsPage cms) {
        this.cms = cms;
    }

    public abstract String additionalTitle();
}
