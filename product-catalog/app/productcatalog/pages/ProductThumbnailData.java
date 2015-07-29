package productcatalog.pages;

import common.prices.PriceFinder;
import common.utils.PriceFormatter;
import common.utils.Translator;
import io.sphere.sdk.models.Image;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

public class ProductThumbnailData {
    private final Translator translator;
    private final PriceFormatter priceFormatter;
    private final PriceFinder priceFinder;

    private final ProductProjection product;
    private final ProductVariant variant;


    public ProductThumbnailData(final Translator translator, final PriceFormatter priceFormatter, final PriceFinder priceFinder, final ProductProjection product, final ProductVariant variant) {
        this.translator = translator;
        this.priceFormatter = priceFormatter;
        this.priceFinder = priceFinder;

        this.product = product;
        this.variant = variant;
    }

    public String getText() {
        return translator.translate(product.getName());
    }

    public String getDescription() {
        return product.getDescription().map(translator::translate)
                .orElse("");
    }

    public String getImage() {
        return variant.getImages().stream().findFirst().map(Image::getUrl).orElse(null);
    }

    public String getPrice() {
        return priceFinder.findPrice(variant.getPrices())
                .map(price -> priceFormatter.format(price.getValue()))
                .orElse("");
    }
}
