package com.commercetools.sunrise.core.localization;

import com.commercetools.sunrise.models.project.CachedProject;
import io.sphere.sdk.client.SphereTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Configuration;
import play.i18n.Lang;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

@Singleton
public final class DefaultCurrencies implements Currencies {

    private static final Logger LOGGER = LoggerFactory.getLogger(Countries.class);
    private static final CurrencyUnit SYSTEM_DEFAULT_CURRENCY = Monetary.getCurrency(Lang.defaultLang().toLocale());

    private final List<CurrencyUnit> availables;

    DefaultCurrencies(final List<CurrencyUnit> availables) {
        this.availables = availables;
    }

    @Inject
    DefaultCurrencies(final Configuration configuration, final CachedProject cachedProject) {
        this(configuredCurrencies(configuration)
                .map(countries -> countries.stream()
                        .map(Monetary::getCurrency)
                        .collect(toList()))
                .orElseGet(() -> loadFallbackCurrencies(cachedProject)));
    }

    @Override
    public List<CurrencyUnit> availables() {
        return availables;
    }

    @Override
    public CurrencyUnit preferred(final List<CurrencyUnit> candidates) {
        return candidates.stream()
                .filter(availables::contains)
                .findFirst()
                .orElseGet(() -> availables.stream()
                        .findFirst()
                        .orElse(SYSTEM_DEFAULT_CURRENCY));
    }

    private static Optional<List<String>> configuredCurrencies(final Configuration configuration) {
        final List<String> currencies = configuration.getStringList("sunrise.localization.currencies", emptyList());
        return currencies.isEmpty() ? Optional.empty() : Optional.of(currencies);
    }

    private static List<CurrencyUnit> loadFallbackCurrencies(final CachedProject cachedProject) {
        try {
            final List<CurrencyUnit> projectCurrencies = cachedProject.blockingGet().getCurrencyUnits();
            return projectCurrencies.isEmpty() ? singletonList(SYSTEM_DEFAULT_CURRENCY) : projectCurrencies;
        } catch (SphereTimeoutException e) {
            LOGGER.warn("Currencies from CTP could not be provided, falling back to default currency", e);
            return singletonList(SYSTEM_DEFAULT_CURRENCY);
        }
    }
}
