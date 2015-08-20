package common.pages;

import common.prices.PriceFinder;
import common.utils.PriceFormatter;
import common.utils.Translator;
import io.sphere.sdk.products.Image;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

import java.util.Optional;

public class ProductThumbnailDataFactory {

    private final Translator translator;
    private final PriceFinder priceFinder;
    private final PriceFormatter priceFormatter;

    private ProductThumbnailDataFactory(final Translator translator, final PriceFinder priceFinder, final PriceFormatter priceFormatter) {
        this.translator = translator;
        this.priceFinder = priceFinder;
        this.priceFormatter = priceFormatter;
    }

    public static ProductThumbnailDataFactory of(final Translator translator, final PriceFinder priceFinder, final PriceFormatter priceFormatter) {
        return new ProductThumbnailDataFactory(translator, priceFinder, priceFormatter);
    }

    public ProductThumbnailData create(final ProductProjection product) {
        final ProductVariant variant = product.getMasterVariant();
        final String text = translator.findTranslation(product.getName());
        final String description = Optional.ofNullable(product.getDescription()).map(translator::findTranslation).orElse("");
        final String imageUrl = variant.getImages().stream().findFirst().map(Image::getUrl).orElse("");
        final String price = priceFinder.findPrice(variant.getPrices())
                .map(foundPrice -> priceFormatter.format(foundPrice.getValue()))
                .orElse("");

        return new ProductThumbnailData(text, description, imageUrl, price);
    }
}
