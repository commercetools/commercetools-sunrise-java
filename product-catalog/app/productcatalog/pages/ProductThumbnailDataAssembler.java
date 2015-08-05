package productcatalog.pages;

import common.cms.CmsPage;
import common.prices.PriceFinder;
import common.utils.PriceFormatter;
import common.utils.Translator;
import io.sphere.sdk.models.Image;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

public class ProductThumbnailDataAssembler {

    private final CmsPage cms;
    private final Translator translator;
    private final PriceFinder priceFinder;
    private final PriceFormatter priceFormatter;

    private ProductThumbnailDataAssembler(final CmsPage cms, final Translator translator, final PriceFinder priceFinder, final PriceFormatter priceFormatter) {
        this.cms = cms;
        this.translator = translator;
        this.priceFinder = priceFinder;
        this.priceFormatter = priceFormatter;
    }

    public static ProductThumbnailDataAssembler of(final CmsPage cms, final Translator translator, final PriceFinder priceFinder, final PriceFormatter priceFormatter) {
        return new ProductThumbnailDataAssembler(cms, translator, priceFinder, priceFormatter);
    }

    public ProductThumbnailData assembleThumbnailData(final ProductProjection product) {
        final ProductVariant variant = product.getMasterVariant();
        final String text = translator.translate(product.getName());
        final String description = product.getDescription().map(translator::translate).orElse("");
        final String imageUrl = variant.getImages().stream().findFirst().map(Image::getUrl).orElse("");
        final String price = priceFinder.findPrice(variant.getPrices())
                .map(foundPrice -> priceFormatter.format(foundPrice.getValue()))
                .orElse("");

        return new ProductThumbnailData(text, description, imageUrl, price);
    }
}
