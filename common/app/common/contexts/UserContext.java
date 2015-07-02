package common.contexts;

import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.customergroups.CustomerGroup;
import io.sphere.sdk.models.Reference;
import org.javamoney.moneta.CurrencyUnitBuilder;

import javax.money.CurrencyContext;
import javax.money.CurrencyContextBuilder;
import javax.money.CurrencyUnit;
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
    private final Optional<Reference<CustomerGroup>> customerGroup;

    private UserContext(final Locale language, final List<Locale> fallbackLanguages, final CountryCode country,
                        final Reference<CustomerGroup> customerGroup) {
        this.language = language;
        this.country = country;
        this.fallbackLanguages = fallbackLanguages;
        this.customerGroup = Optional.ofNullable(customerGroup);
    }

    public Locale language() {
        return language;
    }

    public CountryCode country() {
        return country;
    }

    public List<Locale> fallbackLanguages() {
        return fallbackLanguages;
    }

    public Optional<Reference<CustomerGroup>> customerGroup() {
        return customerGroup;
    }

    public CurrencyUnit currency() {
        final CurrencyContext currencyContext = CurrencyContextBuilder.of("").build();
        return CurrencyUnitBuilder.of(country.getCurrency().getCurrencyCode(), currencyContext).build();
    }

    public static UserContext of(final Locale language, final List<Locale> fallbackLanguages, final CountryCode country) {
        return new UserContext(language, fallbackLanguages, country, null);
    }

    public static UserContext of(final Locale language, final List<Locale> fallbackLanguages, final CountryCode country,
                                 final Reference<CustomerGroup> customerGroup) {
        return new UserContext(language, fallbackLanguages, country, customerGroup);
    }
}
