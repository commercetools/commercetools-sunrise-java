package common.contexts;

import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.channels.Channel;
import io.sphere.sdk.customergroups.CustomerGroup;
import io.sphere.sdk.models.Reference;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * A container for all information related to the current user, such as selected country, language or customer group.
 */
public class UserContext {
    private final Locale language;
    private final List<Locale> fallbackLanguages;
    private final CountryCode country;
    private final ZoneId zoneId;
    private final Optional<Reference<CustomerGroup>> customerGroup;
    private final Optional<Reference<Channel>> channel;

    private UserContext(final Locale language, final List<Locale> fallbackLanguages,
                        final CountryCode country, final ZoneId zoneId,
                        final Reference<CustomerGroup> customerGroup, final Reference<Channel> channel) {
        this.language = language;
        this.country = country;
        this.zoneId = zoneId;
        this.fallbackLanguages = fallbackLanguages;
        this.customerGroup = Optional.ofNullable(customerGroup);
        this.channel = Optional.ofNullable(channel);
    }

    public Locale language() {
        return language;
    }

    public CountryCode country() {
        return country;
    }

    public ZoneId zoneId() {
        return zoneId;
    }

    public List<Locale> fallbackLanguages() {
        return fallbackLanguages;
    }

    public Optional<Reference<CustomerGroup>> customerGroup() {
        return customerGroup;
    }

    public Optional<Reference<Channel>> channel() {
        return channel;
    }

    public CurrencyUnit currency() {
        return Monetary.getCurrency("EUR");
    }

    public static UserContext of(final Locale language, final List<Locale> fallbackLanguages,
                                 final CountryCode country, final ZoneId zoneId) {
        return new UserContext(language, fallbackLanguages, country, zoneId, null, null);
    }

    public static UserContext of(final Locale language, final List<Locale> fallbackLanguages,
                                 final CountryCode country, final ZoneId zoneId,
                                 final Reference<CustomerGroup> customerGroup, final Reference<Channel> channel) {
        return new UserContext(language, fallbackLanguages, country, zoneId, customerGroup, channel);
    }
}
