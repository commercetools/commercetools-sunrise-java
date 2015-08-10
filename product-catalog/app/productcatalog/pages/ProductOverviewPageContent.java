package productcatalog.pages;

import common.pages.PageContent;
import common.pages.ProductThumbnailData;

import java.util.List;

public class ProductOverviewPageContent extends PageContent {

    private final String additionalTitle;
    private final List<ProductThumbnailData>  productList;

    public ProductOverviewPageContent(final String additionalTitle, final List<ProductThumbnailData> productList) {
        this.additionalTitle = additionalTitle;
        this.productList = productList;
    }

    @Override
    public String additionalTitle() {
        return additionalTitle;
    }

    public List<ProductThumbnailData>  getProducts() {
        return productList;
    }
}
