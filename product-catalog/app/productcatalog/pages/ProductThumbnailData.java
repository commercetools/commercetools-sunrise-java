package productcatalog.pages;

import com.neovisionaries.i18n.CountryCode;
import common.contexts.AppContext;
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
        return foo(product.getName());
    }

    public String getDescription() {
        return product.getDescription().map(this::foo).orElse("");
    }

    public String getImage() {
        return variant.getImages().stream().findFirst().map(Image::getUrl).orElse("");
    }

    /**
     * currency should always match
     country, customer group and channel.
     customer group and channel
     customer group and country
     customer group
     country and channel
     channel
     country
     any left
     */

//    public String getPrice() {
//        return variant.getPrices().stream().filter(price -> {
//                    return price.getCountry().map(p -> p.equals(userContext.country())).orElse(false)
//                            && price..getChannel().map(c -> c.equals("")).orElse(false)
//                            && price.getCustomerGroup().map(c -> c.equals(userContext.customerGroup())).orElse(false);
//                }
//        ).findFirst()
//            .orElse(
//                    variant.getPrices().stream().filter(price -> {
//                                return price.getChannel().map(c -> c.equals("")).orElse(false)
//                                        && price.getCustomerGroup().map(c -> c.equals(userContext.customerGroup())).orElse(false);
//                            }
//                    )
//                            .map(p -> priceFormatter.format(p.getValue().)).orElse("");
//    }
//
    public String foo(final LocalizedStrings localizedStrings) {
        return localizedStrings.get(context.user().language())
                .orElse(localizedStrings.get(context.user().fallbackLanguages())
                        .orElse(localizedStrings.get(context.project().languages()).orElse("")));
    }

//    private boolean matches(final Price price, final Optional<CountryCode> countryMatcher, final Optional<Channel> channelMatcher,
//                            final Optional<Reference<CustomerGroup>> customerGroupMatcher) {
//        countryMatcher.map(country -> matchesCountry(price.getCountry(), country)).orElse(true)
//                && channelMatcher.map(channel -> matchesChannel(price.getChannel(), cha))
//    }

    private boolean matchesCountry(final Optional<CountryCode> priceCountry, final CountryCode userCountry) {
        return priceCountry.map(country -> country.equals(userCountry)).orElse(false);
    }

    private boolean matchesChannel(final Optional<Channel> priceChannel, final Optional<Channel> userChannel) {
        return priceChannel.flatMap(channel -> userChannel.map(channel::hasSameIdAs)).orElse(false);
    }

    private boolean matchesCustomerGroup(final Optional<Reference<CustomerGroup>> priceCustomerGroup,
                                         final Optional<Reference<CustomerGroup>> userCustomerGroup) {
        return priceCustomerGroup.flatMap(pcg -> userCustomerGroup.map(pcg::hasSameIdAs)).orElse(false);
    }

    private boolean matchesCurrency(final CurrencyUnit priceCurrency, final CurrencyUnit userCurrency) {
        return priceCurrency.getCurrencyCode().equals(userCurrency.getCurrencyCode());
    }

}
