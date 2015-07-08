package common.prices;

import com.google.common.base.Predicate;
import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.channels.Channel;
import io.sphere.sdk.customergroups.CustomerGroup;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.products.Price;

import javax.money.CurrencyUnit;
import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;

import static com.google.common.base.Predicates.and;

public class PriceScope {

    final Optional<CurrencyUnit> currency;
    final Optional<CountryCode> country;
    final Optional<Reference<CustomerGroup>> customerGroup;
    final Optional<Reference<Channel>> channel;
    final Optional<Instant> date;

    private Predicate<Price> currencyPredicate = price -> this.currency.map(c -> priceHasCurrency(price, c)).orElse(true);
    private Predicate<Price> countryPredicate = price -> this.country.map(c -> priceHasCountry(price, c)).orElse(true);
    private Predicate<Price> customerGroupPredicate = price -> this.customerGroup.map(c -> priceHasCustomerGroup(price, c)).orElse(true);
    private Predicate<Price> channelPredicate = price -> this.channel.map(c -> priceHasChannel(price, c)).orElse(true);
    private Predicate<Price> datePredicate = price -> this.date.map(d -> priceHasValidDate(price, d)).orElse(true);
    private Predicate<Price> dateFallbackPredicate = this::priceHasNoDate;

    PriceScope(final PriceScopeBuilder builder) {
        this.currency = builder.currency;
        this.country = builder.country;
        this.customerGroup = builder.customerGroup;
        this.channel = builder.channel;
        this.date = builder.date;
    }

    public Predicate<Price> predicates() {
        return and(predicatesWithoutDate(), this.datePredicate);
    }

    public Predicate<Price> predicatesWithDateFallback() {
        return and(predicatesWithoutDate(), this.dateFallbackPredicate);
    }

    private Predicate<Price> predicatesWithoutDate() {
        return and(Arrays.asList(currencyPredicate, countryPredicate, customerGroupPredicate, channelPredicate));
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

    private boolean priceHasValidDate(Price price, Instant date) {
        // find in priority a price with a period
        if(!price.getValidFrom().isPresent() & !price.getValidUntil().isPresent())
            return false;

        final boolean toEarly = price.getValidFrom().map(from -> from.isAfter(date)).orElse(false);
        final boolean toLate = price.getValidUntil().map(until -> until.isBefore(date)).orElse(false);

        return !toEarly & !toLate;
    }

    private boolean priceHasNoDate(Price price) {
        return !price.getValidFrom().isPresent() && !price.getValidUntil().isPresent();
    }
}
