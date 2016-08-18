package com.commercetools.sunrise.common.contexts;

import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.channels.Channel;
import io.sphere.sdk.customergroups.CustomerGroup;
import io.sphere.sdk.models.Reference;
import org.junit.Test;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static com.neovisionaries.i18n.CountryCode.UK;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Locale.ENGLISH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UserContextTest {

    private static final CurrencyUnit GBP = Monetary.getCurrency("GBP");

    @Test
    public void findsFirstValidLocale() throws Exception {
        final UserContext userContext = createUserContext(asList(null, ENGLISH, null), UK, GBP);
        assertThat(userContext.locale()).isEqualTo(ENGLISH);
    }

    @Test
    public void throwsExceptionOnEmptyLocales() throws Exception {
        assertThatThrownBy(() -> createUserContext(emptyList(), UK, GBP).locale())
                .isInstanceOf(NoLocaleFoundException.class)
                .hasMessageContaining("User does not have any valid locale associated");
    }

    @Test
    public void throwsExceptionOnOnlyNullLocales() throws Exception {
        assertThatThrownBy(() -> createUserContext(asList(null, null, null), UK, GBP).locale())
                .isInstanceOf(NoLocaleFoundException.class)
                .hasMessageContaining("User does not have any valid locale associated");
    }

    private UserContext createUserContext(final List<Locale> locales, final CountryCode country, final CurrencyUnit currency) {
        return new UserContext() {
            @Override
            public CountryCode country() {
                return country;
            }

            @Override
            public List<Locale> locales() {
                return locales;
            }

            @Override
            public CurrencyUnit currency() {
                return currency;
            }

            @Override
            public Optional<Reference<CustomerGroup>> customerGroup() {
                return Optional.empty();
            }

            @Override
            public Optional<Reference<Channel>> channel() {
                return Optional.empty();
            }
        };
    }
}