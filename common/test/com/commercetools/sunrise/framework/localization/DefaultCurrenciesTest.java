package com.commercetools.sunrise.framework.localization;

import io.sphere.sdk.projects.Project;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import play.Configuration;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import static java.util.Arrays.asList;
import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultCurrenciesTest {

    private static final CurrencyUnit DEFAULT_CURRENCY = Monetary.getCurrency(Locale.getDefault());

    private static final CurrencyUnit CNY = Monetary.getCurrency("CNY");
    private static final CurrencyUnit JPY = Monetary.getCurrency("JPY");
    private static final CurrencyUnit INR = Monetary.getCurrency("INR");
    private static final CurrencyUnit USD = Monetary.getCurrency("USD");
    private static final CurrencyUnit GBP = Monetary.getCurrency("GBP");

    @Mock
    private Project project;

    @Test
    public void takesAvailableFromConfig() {
        mockProjectWithAsianCurrencies();
        final List<String> configCurrencies = asList("USD", "GBP");
        testAvailables(configCurrencies, currencies -> assertThat(currencies).containsExactly(USD, GBP));
    }

    @Test
    public void takesAvailableFromProjectIfNotConfigured() {
        mockProjectWithAsianCurrencies();
        final List<String> configCurrencies = emptyList();
        testAvailables(configCurrencies, currencies -> assertThat(currencies).containsExactly(CNY, JPY, INR));
    }

    @Test
    public void takesAvailableFromSystemAsFallback() {
        mockProjectWithoutCurrencies();
        final List<String> configCurrencies = emptyList();
        testAvailables(configCurrencies, currencies -> assertThat(currencies).containsExactly(DEFAULT_CURRENCY));
    }

    @Test
    public void selectsPreferredCandidate() {
        final Currencies currencies = currenciesWithAvailables(asList(CNY, JPY));
        assertThat(currencies.preferred(asList(JPY, GBP))).isEqualTo(JPY);
        assertThat(currencies.preferred(asList(GBP, JPY))).isEqualTo(JPY);
    }

    @Test
    public void selectsFirstAvailableIfNotMatchingCandidate() {
        final Currencies currencies = currenciesWithAvailables(asList(CNY, JPY));
        assertThat(currencies.preferred(singletonList(GBP))).isEqualTo(CNY);
    }

    @Test
    public void selectsSystemCurrencyAsFallback() {
        final Currencies currencies = currenciesWithAvailables(emptyList());
        assertThat(currencies.preferred(singletonList(GBP))).isEqualTo(DEFAULT_CURRENCY);
    }

    private void mockProjectWithoutCurrencies() {
        when(project.getCurrencyUnits()).thenReturn(emptyList());
    }

    private void mockProjectWithAsianCurrencies() {
        when(project.getCurrencyUnits()).thenReturn(asList(CNY, JPY, INR));
    }

    private DefaultCurrencies currenciesWithAvailables(final List<CurrencyUnit> availables) {
        return new DefaultCurrencies(availables);
    }

    private void testAvailables(final List<String> configCurrencies, final Consumer<List<CurrencyUnit>> test) {
        final Configuration config = new Configuration(singletonMap("sunrise.localization.currencies", configCurrencies));
        final Currencies currencies = new DefaultCurrencies(config, () -> project);
        test.accept(currencies.availables());
    }
}
