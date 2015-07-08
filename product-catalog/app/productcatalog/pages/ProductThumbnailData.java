package productcatalog.pages;

import com.neovisionaries.i18n.CountryCode;
import common.contexts.AppContext;
import common.prices.PriceFinder;
import common.utils.PriceFormatter;
import io.sphere.sdk.channels.Channel;
import io.sphere.sdk.customergroups.CustomerGroup;
import io.sphere.sdk.models.Image;
import io.sphere.sdk.models.LocalizedStrings;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.products.Price;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

import javax.money.CurrencyUnit;
import java.util.Optional;

public class ProductThumbnailData {
    private final ProductProjection product;
    private final ProductVariant variant;
    private final AppContext context;
    private final PriceFormatter priceFormatter;

    public ProductThumbnailData(final ProductProjection product, final ProductVariant variant,
                                final AppContext context, final PriceFormatter priceFormatter) {
        this.product = product;
        this.variant = variant;
        this.context = context;
        this.priceFormatter = priceFormatter;
    }

    public String getText() {
        return findSuitableLanguage(product.getName());
    }

    public String getDescription() {
        return product.getDescription().map(this::findSuitableLanguage).orElse("");
    }

    public String getImage() {
        return variant.getImages().stream().findFirst().map(Image::getUrl).orElse("");
    }

    public String getPrice() {
        return PriceFinder.of(variant).findPriceForContext(context.user())
                .transform(price -> priceFormatter.format(price.getValue(), context))
                .or("");
    }

    public String findSuitableLanguage(final LocalizedStrings localizedStrings) {
        return localizedStrings.get(context.user().language())
                .orElse(localizedStrings.get(context.user().fallbackLanguages())
                        .orElse(localizedStrings.get(context.project().languages()).orElse("")));
    }
}
