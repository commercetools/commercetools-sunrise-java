package productcatalog.pages;

import common.pages.PageContent;

public class ProductOverviewPageContent extends PageContent {
    private final String additionalTitle;
    private final ProductListData productListData;
    private final FilterListData filterListData;

    public ProductOverviewPageContent(final String additionalTitle, final ProductListData productListData, final FilterListData filterListData) {
        this.additionalTitle = additionalTitle;
        this.productListData = productListData;
        this.filterListData = filterListData;
    }

    @Override
    public String additionalTitle() {
        return additionalTitle;
    }

    public FilterListData getFilters() {
        return filterListData;
    }

    public ProductListData getProducts() {
        return productListData;
    }
}
