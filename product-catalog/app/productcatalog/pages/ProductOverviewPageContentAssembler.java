package productcatalog.pages;

import common.cms.CmsPage;
import common.prices.PriceFinder;
import common.utils.PriceFormatter;
import common.utils.Translator;
import io.sphere.sdk.products.ProductProjection;

import java.util.List;

public class ProductOverviewPageContentAssembler {

    private final CmsPage cms;
    private final ProductListDataAssembler productListDataAssembler;

    private ProductOverviewPageContentAssembler(final CmsPage cms, final Translator translator, final PriceFinder priceFinder, final PriceFormatter priceFormatter) {
        this.cms = cms;
        productListDataAssembler = ProductListDataAssembler.of(cms, translator, priceFinder, priceFormatter);

    }

    public static ProductOverviewPageContentAssembler of(final CmsPage cms, final Translator translator, final PriceFinder priceFinder, final PriceFormatter priceFormatter) {
        return new ProductOverviewPageContentAssembler(cms, translator, priceFinder, priceFormatter);
    }

    public ProductOverviewPageContent assemblePopContent(final List<ProductProjection> products) {
        return new ProductOverviewPageContent(cms, "", assembleProductListData(products));
    }

    private ProductListData assembleProductListData(final List<ProductProjection> products) {
        return productListDataAssembler.assembleProductListData(products);
    }
}
