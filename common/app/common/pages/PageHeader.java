package common.pages;

import common.cms.CmsPage;

public class PageHeader {
    private final CmsPage cms;
    private final String title;

    public PageHeader(final CmsPage cms, final String title) {
        this.cms = cms;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public LinkData getStores() {
        return new LinkData(cms.getOrEmpty("header.stores"), "");
    }

    public LinkData getHelp() {
        return new LinkData(cms.getOrEmpty("header.help"), "");
    }

    public String getCallUs() {
        return cms.getOrEmpty("header.callUs");
    }


}
