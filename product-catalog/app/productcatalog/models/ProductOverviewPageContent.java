package productcatalog.models;

import common.cms.CmsPage;
import common.pages.PageContent;

public class ProductOverviewPageContent extends PageContent {

    public ProductOverviewPageContent(final CmsPage cms) {
        super(cms);
    }

    @Override
    public String additionalTitle() {
        return cms.getOrEmpty("title");
    }
}
