package productcatalog.models;

import common.cms.CmsPage;
import common.pages.PageData;

public class ProductOverviewPageData extends PageData {

    public ProductOverviewPageData(final CmsPage cms) {
        super(cms);
    }

    @Override
    protected String additionalTitle() {
        return "Product overview page";
    }
}
