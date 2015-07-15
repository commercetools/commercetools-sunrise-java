package productcatalog.pages;

import common.cms.CmsPage;
import common.contexts.AppContext;
import common.pages.PageContent;
import common.utils.PriceFormatter;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.PagedSearchResult;

public class ProductOverviewPageContent extends PageContent {
    private final AppContext context;
    private final PriceFormatter priceFormatter;
    private final PagedSearchResult<ProductProjection> productList;

    public ProductOverviewPageContent(final CmsPage cms, final AppContext context, final PagedSearchResult<ProductProjection> productList, final PriceFormatter priceFormatter) {
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
        return new ProductListData(productList.getResults(), context, priceFormatter);
    }
}
