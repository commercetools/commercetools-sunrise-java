package productcatalog.pages;

import common.cms.CmsPage;
import common.contexts.AppContext;
import common.pages.PageContent;
import common.utils.PriceFormatter;
import io.sphere.sdk.products.ProductProjection;

import java.util.List;

public class ProductOverviewPageContent extends PageContent {
    private final AppContext context;
    private final PriceFormatter priceFormatter;
    private final List<ProductProjection> productList;

    public ProductOverviewPageContent(final CmsPage cms, final AppContext context, final List<ProductProjection> productList, final PriceFormatter priceFormatter) {
        super(cms);
        this.context = context;
        this.priceFormatter = priceFormatter;
        this.productList = productList;
    }

    @Override
    public String additionalTitle() {
        // TODO Fill with category name?
        return "";
    }

    public ProductListData getProducts() {
        return new ProductListData(productList, context, priceFormatter, "", "", "", "", "", "");
    }
}
