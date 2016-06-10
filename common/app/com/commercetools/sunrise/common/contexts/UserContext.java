package com.commercetools.sunrise.common.contexts;

import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.channels.Channel;
import io.sphere.sdk.customergroups.CustomerGroup;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.models.Reference;

import javax.annotation.Nullable;
import javax.money.CurrencyUnit;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * A container for all information related to the current user, such as selected country, language or customer group.
 */
public class UserContext extends Base {
    private final List<Locale> locales;
    private final CountryCode country;
    private final CurrencyUnit currency;
    private final Optional<Reference<CustomerGroup>> customerGroup;
    private final Optional<Reference<Channel>> channel;

    private UserContext(final List<Locale> locales, final CountryCode country, final CurrencyUnit currency,
                        @Nullable final Reference<CustomerGroup> customerGroup, @Nullable final Reference<Channel> channel) {
        this.locales = locales;
        this.country = country;
        this.currency = currency;
        this.customerGroup = Optional.ofNullable(customerGroup);
        this.channel = Optional.ofNullable(channel);
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

    public CurrencyUnit currency() {
        return currency;
    }

    public Optional<Reference<CustomerGroup>> customerGroup() {
        return customerGroup;
    }

    public Optional<Reference<Channel>> channel() {
        return channel;
    }

    public static UserContext of(final List<Locale> locales, final CountryCode country, final CurrencyUnit currency) {
        return new UserContext(locales, country, currency, null, null);
    }

    public static UserContext of(final List<Locale> locales, final CountryCode country, final CurrencyUnit currency,
                                 @Nullable final Reference<CustomerGroup> customerGroup, @Nullable final Reference<Channel> channel) {
        return new UserContext(locales, country, currency, customerGroup, channel);
    }
}
