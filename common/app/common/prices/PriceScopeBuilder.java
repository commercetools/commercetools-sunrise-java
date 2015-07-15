package common.prices;

import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.channels.Channel;
import io.sphere.sdk.customergroups.CustomerGroup;
import io.sphere.sdk.models.Reference;

import javax.money.CurrencyUnit;
import java.time.ZonedDateTime;
import java.util.Optional;

public class PriceScopeBuilder {

    Optional<CurrencyUnit> currency = Optional.empty();
    Optional<CountryCode> country = Optional.empty();
    Optional<Reference<CustomerGroup>> customerGroup = Optional.empty();
    Optional<Reference<Channel>> channel = Optional.empty();
    Optional<ZonedDateTime> date = Optional.empty();

    private PriceScopeBuilder() {

    }

    public static PriceScopeBuilder of(final PriceScope scope) {
        final PriceScopeBuilder builder = PriceScopeBuilder.of();
        builder.currency = scope.currency;
        builder.country = scope.country;
        builder.customerGroup = scope.customerGroup;
        builder.channel = scope.channel;
        builder.date = scope.date;
        return builder;
    }

    public static PriceScopeBuilder of() {
        return new PriceScopeBuilder();
    }

    public PriceScopeBuilder currency(final CurrencyUnit currency) {
        this.currency = Optional.of(currency);
        return this;
    }

    public PriceScopeBuilder country(final CountryCode country) {
        this.country = Optional.of(country);
        return this;
    }

    public PriceScopeBuilder customerGroup(final Optional<Reference<CustomerGroup>> customerGroup) {
        this.customerGroup = customerGroup;
        return this;
    }

    public PriceScopeBuilder channel(final Optional<Reference<Channel>> channel) {
        this.channel = channel;
        return this;
    }

    public PriceScopeBuilder date(final ZonedDateTime date) {
        this.date = Optional.of(date);
        return this;
    }

    public PriceScope build() {
        return new PriceScope(this);
    }
}
