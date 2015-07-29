package productcatalog.pages;

import common.cms.CmsPage;
import common.pages.PageContent;
import common.prices.PriceFinder;
import common.utils.PriceFormatter;
import common.utils.Translator;
import io.sphere.sdk.products.ProductProjection;

import java.util.List;

public class ProductOverviewPageContent extends PageContent {
    private final Translator translator;
    private final PriceFormatter priceFormatter;
    private final PriceFinder priceFinder;

    private final List<ProductProjection> productList;

    public ProductOverviewPageContent(final CmsPage cms, final Translator translator, final PriceFormatter priceFormatter, final PriceFinder priceFinder, final List<ProductProjection> productList) {
        super(cms);
        this.translator = translator;
        this.priceFormatter = priceFormatter;
        this.priceFinder = priceFinder;

        this.productList = productList;
    }

    @Override
    public String additionalTitle() {
        // TODO Fill with category name?
        return "";
    }

    public ProductListData getProducts() {
        return new ProductListData(translator, priceFormatter, priceFinder, productList, "", "", "", "", "", "");
    }
}
