package common.prices;

import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.channels.Channel;
import io.sphere.sdk.customergroups.CustomerGroup;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.products.Price;

import javax.money.CurrencyUnit;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

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
    public Optional<Price> findPrice(final Collection<Price> prices,
                                     final CurrencyUnit currency,
                                     final CountryCode country,
                                     final java.util.Optional<Reference<CustomerGroup>> customerGroup,
                                     final java.util.Optional<Reference<Channel>> channel,
                                     final ZonedDateTime userTime) {
        final PriceScope base = PriceScopeBuilder.of().currency(currency).date(userTime).build();
        final List<PriceScope> scopes = asList(
                PriceScopeBuilder.of(base).country(country).customerGroup(customerGroup).channel(channel).build(),
                PriceScopeBuilder.of(base).customerGroup(customerGroup).channel(channel).build(),
                PriceScopeBuilder.of(base).customerGroup(customerGroup).country(country).build(),
                PriceScopeBuilder.of(base).customerGroup(customerGroup).build(),
                PriceScopeBuilder.of(base).country(country).channel(channel).build(),
                PriceScopeBuilder.of(base).channel(channel).build(),
                PriceScopeBuilder.of(base).country(country).build(),
                base);

        return scopes.stream()
                .map(scope -> findPriceForScope(prices, scope))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }

    private Optional<Price> findPriceForScope(final Collection<Price> prices, final PriceScope scope) {
        final List<Price> foundPrices = prices.stream()
                .filter(price -> scope.currency.map(c -> priceHasCurrency(price, c)).orElse(true))
                .filter(price -> scope.country.map(c -> priceHasCountry(price, c)).orElse(true))
                .filter(price -> scope.customerGroup.map(c -> priceHasCustomerGroup(price, c)).orElse(true))
                .filter(price -> scope.channel.map(c -> priceHasChannel(price, c)).orElse(true))
                .collect(Collectors.toList());

        return applyDate(foundPrices, scope.date);
    }

    private Optional<Price> applyDate(final Collection<Price> prices, final Optional<ZonedDateTime> date) {
        final Optional<Price> priceWithDate = prices.stream()
                .filter(price -> date.map(d -> !priceHasNoDate(price) & priceHasValidDate(price, d)).orElse(true))
                .findFirst();

        if (priceWithDate.isPresent())
            return priceWithDate;
        else
            return prices.stream().filter(this::priceHasNoDate).findFirst();
    }

    private boolean priceHasCurrency(Price price, CurrencyUnit currency) {
        return price.getValue().getCurrency().compareTo(currency) == 0;
    }

    private boolean priceHasCountry(Price price, CountryCode country) {
        return price.getCountry().map(c -> c.compareTo(country) == 0).orElse(false);
    }

    private boolean priceHasCustomerGroup(Price price, Reference<CustomerGroup> customerGroup) {
        return price.getCustomerGroup().map(c -> c.hasSameIdAs(customerGroup)).orElse(false);
    }

    private boolean priceHasChannel(Price price, Reference<Channel> channel) {
        return price.getChannel().map(c -> c.hasSameIdAs(channel)).orElse(false);
    }

    private boolean priceHasValidDate(Price price, ZonedDateTime date) {
        final boolean toEarly = price.getValidFrom().map(from -> from.isAfter(date)).orElse(false);
        final boolean toLate = price.getValidUntil().map(until -> until.isBefore(date)).orElse(false);

        return !toEarly & !toLate;
    }

    private boolean priceHasNoDate(Price price) {
        return !price.getValidFrom().isPresent() && !price.getValidUntil().isPresent();
    }

}
