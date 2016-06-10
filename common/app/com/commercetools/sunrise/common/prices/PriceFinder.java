package com.commercetools.sunrise.common.prices;

import com.neovisionaries.i18n.CountryCode;
import com.commercetools.sunrise.common.contexts.UserContext;
import io.sphere.sdk.channels.Channel;
import io.sphere.sdk.customergroups.CustomerGroup;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.products.Price;

import javax.money.CurrencyUnit;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class PriceFinder {

    final CurrencyUnit currency;
    final CountryCode country;
    final Optional<Reference<CustomerGroup>> customerGroup;
    final Optional<Reference<Channel>> channel;

    private PriceFinder(final CurrencyUnit currency, final CountryCode country, final Optional<Reference<CustomerGroup>> customerGroup,
                        final Optional<Reference<Channel>> channel) {
        this.currency = currency;
        this.country = country;
        this.customerGroup = customerGroup;
        this.channel = channel;
    }

    public static PriceFinder of(final CurrencyUnit currency, final CountryCode country, final Optional<Reference<CustomerGroup>> customerGroup, final Optional<Reference<Channel>> channel) {
        return new PriceFinder(currency, country, customerGroup, channel);
    }

    public static PriceFinder of(final UserContext userContext) {
        return of(userContext.currency(), userContext.country(), userContext.customerGroup(), userContext.channel());
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
    public Optional<Price> findPrice(final List<Price> prices) {
        final PriceScope base = PriceScopeBuilder.of().currency(currency).date(ZonedDateTime.now()).build();
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

    private Optional<Price> findPriceForScope(final List<Price> prices, final PriceScope scope) {
        final List<Price> foundPrices = prices.stream()
                .filter(price -> scope.currency.map(c -> priceHasCurrency(price, c)).orElse(true))
                .filter(price -> scope.country.map(c -> priceHasCountry(price, c)).orElse(true))
                .filter(price -> scope.customerGroup.map(c -> priceHasCustomerGroup(price, c)).orElse(true))
                .filter(price -> scope.channel.map(c -> priceHasChannel(price, c)).orElse(true))
                .collect(Collectors.toList());

        return findCurrentPrice(foundPrices, scope.date);
    }

    private Optional<Price> findCurrentPrice(final List<Price> prices, final Optional<ZonedDateTime> date) {
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
        return Optional.ofNullable(price.getCountry()).map(c -> c.compareTo(country) == 0).orElse(false);
    }

    private boolean priceHasCustomerGroup(Price price, Reference<CustomerGroup> customerGroup) {
        return Optional.ofNullable(price.getCustomerGroup()).map(c -> c.hasSameIdAs(customerGroup)).orElse(false);
    }

    private boolean priceHasChannel(Price price, Reference<Channel> channel) {
        return Optional.ofNullable(price.getChannel()).map(c -> c.hasSameIdAs(channel)).orElse(false);
    }

    private boolean priceHasValidDate(Price price, ZonedDateTime date) {
        final boolean tooEarly = Optional.ofNullable(price.getValidFrom()).map(from -> from.isAfter(date)).orElse(false);
        final boolean tooLate = Optional.ofNullable(price.getValidUntil()).map(until -> until.isBefore(date)).orElse(false);

        return !tooEarly && !tooLate;
    }

    private boolean priceHasNoDate(Price price) {
        return !Optional.ofNullable(price.getValidFrom()).isPresent()
                && !Optional.ofNullable(price.getValidUntil()).isPresent();
    }
}
