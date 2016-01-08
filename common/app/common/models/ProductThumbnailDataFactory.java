package common.models;

import common.prices.PriceFinderFactory;
import common.contexts.UserContext;
import common.prices.PriceFinder;
import common.utils.PriceFormatter;
import io.sphere.sdk.products.Image;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class ProductThumbnailDataFactory {

    private final PriceFinder priceFinder;
    private final List<Locale> locales;
    private final PriceFormatter priceFormatter;

    private ProductThumbnailDataFactory(final UserContext userContext) {
        this.locales = userContext.locales();
        this.priceFinder = PriceFinderFactory.create(userContext);
        this.priceFormatter = PriceFormatter.of(userContext.locale());
    }

    public ProductThumbnailData create(final ProductProjection product) {
        final ProductVariant variant = product.getMasterVariant();
        final String text = product.getName().find(locales).orElse("");
        final String description = Optional.ofNullable(product.getDescription()).flatMap(d -> d.find(locales)).orElse("");
        final String imageUrl = variant.getImages().stream().findFirst().map(Image::getUrl).orElse("");
        final String price = priceFinder.findPrice(variant.getPrices())
                .map(foundPrice -> priceFormatter.format(foundPrice.getValue()))
                .orElse("");

        return new ProductThumbnailData(text, description, imageUrl, price);
    }

    public static ProductThumbnailDataFactory of(final UserContext userContext) {
        return new ProductThumbnailDataFactory(userContext);
    }
}
