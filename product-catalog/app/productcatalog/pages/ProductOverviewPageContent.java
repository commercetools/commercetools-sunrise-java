package productcatalog.pages;

import common.cms.CmsPage;
import common.pages.PageContent;

public class ProductOverviewPageContent extends PageContent {

    private final String additionalTitle;
    private final ProductListData productList;

    public ProductOverviewPageContent(final CmsPage cms, final String additionalTitle, final ProductListData productList) {
        super(cms);
        this.additionalTitle = additionalTitle;
        this.productList = productList;
    }

    @Override
    public String additionalTitle() {
        return additionalTitle;
    }

    public ProductListData getProducts() {
        return productList;
    }
}
