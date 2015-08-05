package productcatalog.pages;

import common.cms.CmsPage;
import common.prices.PriceFinder;
import common.utils.PriceFormatter;
import common.utils.Translator;
import io.sphere.sdk.products.ProductProjection;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class ProductListDataAssembler {

    private final CmsPage cms;

    private final ProductThumbnailDataAssembler productThumbnailDataAssembler;

    private ProductListDataAssembler(final CmsPage cms, final Translator translator, final PriceFinder priceFinder, final PriceFormatter priceFormatter) {
        this.cms = cms;
        productThumbnailDataAssembler = ProductThumbnailDataAssembler.of(cms, translator, priceFinder, priceFormatter);
    }

    public static ProductListDataAssembler of(final CmsPage cms, final Translator translator, final PriceFinder priceFinder, final PriceFormatter priceFormatter) {
        return new ProductListDataAssembler(cms, translator, priceFinder, priceFormatter);
    }

    public ProductListData assembleProductListData(final String text, final String sale, final String isNew, final String quickView, final String wishlist, final String moreColors, final List<ProductProjection> productList) {
        final List<ProductThumbnailData> thumbnails = productList.stream()
                .map(productThumbnailDataAssembler::assembleThumbnailData)
                .collect(toList());

        return new ProductListData(text, sale, isNew, quickView, wishlist, moreColors, thumbnails);
    }

    public ProductListData assembleProductListData(final List<ProductProjection> productList) {
        return assembleProductListData("", "", "", "", "", "", productList);
    }
}
