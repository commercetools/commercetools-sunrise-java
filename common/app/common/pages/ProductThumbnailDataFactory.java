package common.pages;

import common.contexts.PriceFinderFactory;
import common.contexts.UserContext;
import common.prices.PriceFinder;
import io.sphere.sdk.products.Image;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

import java.util.Optional;

public class ProductThumbnailDataFactory {

    private final PriceFinder priceFinder;
    private final UserContext userContext;

    private ProductThumbnailDataFactory(final UserContext userContext) {
        this.userContext = userContext;
        this.priceFinder = PriceFinderFactory.create(userContext);
    }

    public ProductThumbnailData create(final ProductProjection product) {
        final ProductVariant variant = product.getMasterVariant();
        final String text = userContext.getTranslation(product.getName());
        final String description = Optional.ofNullable(product.getDescription()).map(userContext::getTranslation).orElse("");
        final String imageUrl = variant.getImages().stream().findFirst().map(Image::getUrl).orElse("");
        final String price = priceFinder.findPrice(variant.getPrices())
                .map(foundPrice -> userContext.format(foundPrice.getValue()))
                .orElse("");

        return new ProductThumbnailData(text, description, imageUrl, price);
    }

    public static ProductThumbnailDataFactory of(final UserContext userContext) {
        return new ProductThumbnailDataFactory(userContext);
    }
}
