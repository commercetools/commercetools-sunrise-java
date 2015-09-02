package common.contexts;

import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.channels.Channel;
import io.sphere.sdk.customergroups.CustomerGroup;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.models.Reference;

import javax.annotation.Nullable;
import javax.money.CurrencyUnit;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * A container for all information related to the current user, such as selected country, language or customer group.
 */
public class UserContext extends Base {
    private final CountryCode country;
    private final List<Locale> locales;
    private final ZoneId zoneId;
    private final Optional<Reference<CustomerGroup>> customerGroup;
    private final Optional<Reference<Channel>> channel;
    private final CurrencyUnit currency;

    private UserContext(final CountryCode country, final List<Locale> locales, final ZoneId zoneId, @Nullable final Reference<CustomerGroup> customerGroup, @Nullable final Reference<Channel> channel, final CurrencyUnit currency) {
        this.country = country;
        this.locales = locales;
        this.zoneId = zoneId;
        this.customerGroup = Optional.ofNullable(customerGroup);
        this.channel = Optional.ofNullable(channel);
        this.currency = currency;
        if (locales.isEmpty() || locales.get(0) == null) {
            throw new IllegalArgumentException("Locales must contain at least one valid locale");
        }
    }

    public CountryCode country() {
        return country;
    }

    public Locale locale() {
        return locales.stream().findFirst().orElseThrow(NoLocaleFoundException::new);
    }

    public List<Locale> locales() {
        return locales;
    }

    public ZoneId zoneId() {
        return zoneId;
    }

    public CurrencyUnit currency() {
        return currency;
    }

    public Optional<Reference<CustomerGroup>> customerGroup() {
        return customerGroup;
    }

    public Optional<Reference<Channel>> channel() {
        return channel;
    }

    public static UserContext of(final CountryCode country, final List<Locale> languages, final ZoneId zoneId, final CurrencyUnit currency) {
        return new UserContext(country, languages, zoneId, null, null, currency);
    }

    public static UserContext of(final CountryCode country, final List<Locale> languages, final ZoneId zoneId, final CurrencyUnit currency,
                                 @Nullable final Reference<CustomerGroup> customerGroup, @Nullable final Reference<Channel> channel) {
        return new UserContext(country, languages, zoneId, customerGroup, channel, currency);
    }
}
