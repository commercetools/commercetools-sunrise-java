package productcatalog.pages;

import common.cms.CmsPage;
import common.pages.PageContent;

public class ProductOverviewPageContent extends PageContent {

    public ProductOverviewPageContent(final CmsPage cms) {
        super(cms);
    }

    @Override
    public String additionalTitle() {
        // TODO Fill with category name?
        return "";
    }
}
