package productcatalog.pages;

import common.contexts.AppContext;
import common.prices.PriceFinder;
import common.utils.PriceFormatter;
import io.sphere.sdk.models.Image;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import static common.utils.Languages.*;

public class ProductThumbnailData {
    private final ProductProjection product;
    private final ProductVariant variant;
    private final AppContext context;
    private final PriceFormatter priceFormatter;
    private final PriceFinder priceFinder;

    public ProductThumbnailData(final ProductProjection product, final ProductVariant variant,
                                final AppContext context, final PriceFormatter priceFormatter) {
        this.product = product;
        this.variant = variant;
        this.context = context;
        this.priceFormatter = priceFormatter;
        this.priceFinder = PriceFinder.of(
                context.user().currency(), context.user().country(), context.user().customerGroup(),
                context.user().channel(), ZonedDateTime.of(LocalDateTime.now(), context.user().zoneId()));
    }

    public String getText() {
        return translate(product.getName(), context);
    }

    public String getDescription() {
        return product.getDescription().map(description -> translate(description, context))
                .orElse("");
    }

    public String getImage() {
        return variant.getImages().stream().findFirst().map(Image::getUrl).orElse("http://sphereio.github.io/nodejs/assets/img/sphere_logo_rgb_big.png");
    }

    public String getPrice() {
        return priceFinder.findPrice(variant.getPrices())
                .map(price -> priceFormatter.format(price.getValue(), context))
                .orElse("");
    }
}
