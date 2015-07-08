package common.prices;

import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import com.neovisionaries.i18n.CountryCode;
import common.contexts.UserContext;
import io.sphere.sdk.channels.Channel;
import io.sphere.sdk.customergroups.CustomerGroup;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.products.Price;
import io.sphere.sdk.products.ProductVariant;

import java.time.Instant;

public class PriceFinder {

    private final ProductVariant variant;

    private PriceFinder(final ProductVariant variant) {
        this.variant = variant;
    }

    public static PriceFinder of(final ProductVariant variant) {
        return new PriceFinder(variant);
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
    public Optional<Price> findPriceForContext(final UserContext userContext) {
        final PriceScopeBuilder base = PriceScopeBuilder.of().currency(userContext.currency()).date(Instant.now());
        final CountryCode country = userContext.country();
        final java.util.Optional<Reference<CustomerGroup>> customerGroup = userContext.customerGroup();
        final java.util.Optional<Reference<Channel>> channel = userContext.channel();

        return findPriceForScope(base.country(country).customerGroup(customerGroup).channel(channel).build())
                .or(findPriceForScope(base.customerGroup(customerGroup).channel(channel).build()))
                .or(findPriceForScope(base.customerGroup(customerGroup).country(country).build()))
                .or(findPriceForScope(base.customerGroup(customerGroup).build()))
                .or(findPriceForScope(base.country(country).channel(channel).build()))
                .or(findPriceForScope(base.channel(channel).build()))
                .or(findPriceForScope(base.country(country).build()))
                .or(findPriceForScope(base.build()));
    }

    private Optional<Price> findPriceForScope(final PriceScope scope) {
        return Iterables.tryFind(variant.getPrices(), scope.predicates())
                .or(Iterables.tryFind(variant.getPrices(), scope.predicatesWithDateFallback()));
    }
}
