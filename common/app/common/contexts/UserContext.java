package common.contexts;

import com.neovisionaries.i18n.CountryCode;
import common.utils.TranslationResolver;
import io.sphere.sdk.channels.Channel;
import io.sphere.sdk.customergroups.CustomerGroup;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.models.LocalizedString;
import io.sphere.sdk.models.Reference;

import javax.annotation.Nullable;
import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import javax.money.format.MonetaryFormats;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static java.util.Collections.singletonList;

/**
 * A container for all information related to the current user, such as selected country, language or customer group.
 */
public class UserContext extends Base implements TranslationResolver {
    private final CountryCode country;
    private final Locale language;
    /** includes user and project fallback */
    private final List<Locale> fallbackLanguages;
    private final ZoneId zoneId;
    private final Optional<Reference<CustomerGroup>> customerGroup;
    private final Optional<Reference<Channel>> channel;
    private final CurrencyUnit currency;

    private UserContext(final CountryCode country, final Locale language, final List<Locale> fallbackLanguages, final ZoneId zoneId, @Nullable final Reference<CustomerGroup> customerGroup, @Nullable final Reference<Channel> channel, final CurrencyUnit currency) {
        this.country = country;
        this.language = language;
        this.fallbackLanguages = fallbackLanguages;
        this.zoneId = zoneId;
        this.customerGroup = Optional.ofNullable(customerGroup);
        this.channel = Optional.ofNullable(channel);
        this.currency = currency;
    }

    public static UserContext of(final CountryCode country, final Locale language, final List<Locale> fallbackLanguages, final ZoneId zoneId, final CurrencyUnit currency) {
        return new UserContext(country, language, fallbackLanguages, zoneId, null, null, currency);
    }

    public static UserContext of(final CountryCode country, final Locale language, final List<Locale> fallbackLanguages, final ZoneId zoneId, final CurrencyUnit currency, @Nullable final Reference<CustomerGroup> customerGroup, @Nullable final Reference<Channel> channel) {
        return new UserContext(country, language, fallbackLanguages, zoneId, customerGroup, channel, currency);
    }

    public CountryCode country() {
        return country;
    }

    public Locale language() {
        return language;
    }

    public List<Locale> fallbackLanguages() {
        return fallbackLanguages;
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

    /**
     * Finds the best fitting translation trying the following languages in that order:
     *  - the users preferred language
     *  - one of the users fallback languages
     *  - one of the projects languages
     *
     *  Falls back to an empty String if none is found in the former
     * @param localizedString the source to find the translation
     * @return the found translation or an empty String
     */
    public String getTranslation(final LocalizedString localizedString) {
        return findTranslation(localizedString, singletonList(language))
                .orElse(findTranslation(localizedString, fallbackLanguages)
                        .orElse(""));
    }

    private Optional<String> findTranslation(final LocalizedString localizedString, final List<Locale> locale) {
        return localizedString.find(locale);
    }

    public String format(final MonetaryAmount price) {
        return MonetaryFormats.getAmountFormat(language()).format(price);
    }
}
