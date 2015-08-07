package productcatalog.pages;

import common.contexts.AppContext;
import common.prices.PriceFinder;
import common.utils.PriceFormatter;
import io.sphere.sdk.models.LocalizedString;
import io.sphere.sdk.products.Image;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Optional;

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
        return findSuitableLanguage(product.getName());
    }

    public String getDescription() {
        return Optional.ofNullable(product.getDescription()).map(this::findSuitableLanguage).orElse("");
    }

    public String getImage() {
        return variant.getImages().stream().findFirst().map(Image::getUrl).orElse("");
    }

    public String getPrice() {
        return priceFinder.findPrice(variant.getPrices())
                .map(price -> priceFormatter.format(price.getValue(), context))
                .orElse("");
    }

    public String findSuitableLanguage(final LocalizedString localizedStrings) {
        return Optional.ofNullable(localizedStrings.get(context.user().language()))
                .orElse(Optional.ofNullable(localizedStrings.get(context.user().fallbackLanguages()))
                        .orElse(Optional.ofNullable(localizedStrings.get(context.project().languages())).orElse("")));
    }
}
