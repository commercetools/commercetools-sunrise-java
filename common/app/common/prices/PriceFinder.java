package common.prices;

import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.channels.Channel;
import io.sphere.sdk.customergroups.CustomerGroup;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.products.Price;

import javax.money.CurrencyUnit;
import java.time.ZonedDateTime;
import java.util.List;

public class PriceFinder {

    private PriceFinder() {

    }

    public static PriceFinder of() {
        return new PriceFinder();
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
    public Optional<Price> findPrice(final List<Price> prices,
                                     final CurrencyUnit currency,
                                     final CountryCode country,
                                     final java.util.Optional<Reference<CustomerGroup>> customerGroup,
                                     final java.util.Optional<Reference<Channel>> channel,
                                     final ZonedDateTime userTime) {
        final PriceScopeBuilder base = PriceScopeBuilder.of().currency(currency).date(userTime);

        return findPriceForScope(prices, base.country(country).customerGroup(customerGroup).channel(channel).build())
                .or(findPriceForScope(prices, base.customerGroup(customerGroup).channel(channel).build()))
                .or(findPriceForScope(prices, base.customerGroup(customerGroup).country(country).build()))
                .or(findPriceForScope(prices, base.customerGroup(customerGroup).build()))
                .or(findPriceForScope(prices, base.country(country).channel(channel).build()))
                .or(findPriceForScope(prices, base.channel(channel).build()))
                .or(findPriceForScope(prices, base.country(country).build()))
                .or(findPriceForScope(prices, base.build()));
    }

    private Optional<Price> findPriceForScope(final List<Price> prices, final PriceScope scope) {
        return Iterables.tryFind(prices, scope.predicates())
                .or(Iterables.tryFind(prices, scope.predicatesWithDateFallback()));
    }
}
