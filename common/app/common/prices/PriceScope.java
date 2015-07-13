package common.prices;

import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.channels.Channel;
import io.sphere.sdk.customergroups.CustomerGroup;
import io.sphere.sdk.models.Reference;

import javax.money.CurrencyUnit;
import java.time.ZonedDateTime;
import java.util.Optional;

class PriceScope {

    final Optional<CurrencyUnit> currency;
    final Optional<CountryCode> country;
    final Optional<Reference<CustomerGroup>> customerGroup;
    final Optional<Reference<Channel>> channel;
    final Optional<ZonedDateTime> date;

    PriceScope(final PriceScopeBuilder builder) {
        this.currency = builder.currency;
        this.country = builder.country;
        this.customerGroup = builder.customerGroup;
        this.channel = builder.channel;
        this.date = builder.date;
    }
}