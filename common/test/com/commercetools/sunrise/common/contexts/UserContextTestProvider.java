package com.commercetools.sunrise.common.contexts;

import com.google.inject.Provider;
import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.channels.Channel;
import io.sphere.sdk.customergroups.CustomerGroup;
import io.sphere.sdk.models.Reference;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static java.util.Arrays.asList;

public class UserContextTestProvider implements Provider<UserContext> {

    @Override
    public UserContext get() {
        return new UserContext() {
            @Override
            public CountryCode country() {
                return CountryCode.DE;
            }

            @Override
            public List<Locale> locales() {
                return asList(Locale.ENGLISH, Locale.GERMAN);
            }

            @Override
            public CurrencyUnit currency() {
                return Monetary.getCurrency(CountryCode.DE.getCurrency().getCurrencyCode());
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
