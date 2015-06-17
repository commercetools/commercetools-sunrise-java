package common.pages;

import common.cms.CmsPage;

public class HeaderData {
    private final CmsPage cms;

    public HeaderData(final CmsPage cms) {
        this.cms = cms;
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
