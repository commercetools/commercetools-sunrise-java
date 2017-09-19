package com.commercetools.sunrise.framework.localization;

import com.commercetools.sunrise.play.configuration.SunriseConfigurationException;
import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.client.SphereRequest;
import io.sphere.sdk.client.SphereTimeoutException;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.projects.Project;
import io.sphere.sdk.projects.queries.ProjectGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Configuration;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

import static io.sphere.sdk.client.SphereClientUtils.blockingWait;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

/**
 * Uses the associated commercetools platform project to obtain the supported languages, countries and currencies.
 * Nevertheless it enables the possibility to override these values via configuration.
 */

@Singleton
final class ProjectContextImpl extends Base implements ProjectContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectContext.class);
    private static final String CONFIG_LANGUAGES = "application.i18n.languages";
    private static final String CONFIG_COUNTRIES = "application.countries";
    private static final String CONFIG_CURRENCIES = "application.currencies";

    private final List<Locale> locales;
    private final List<CountryCode> countryCodes;
    private final List<CurrencyUnit> currencies;

    @Inject
    ProjectContextImpl(final Configuration configuration, final SphereClient client) {
        try {
            final SphereRequest<Project> request = ProjectGet.of();
            final Project project = blockingWait(client.execute(request), Duration.ofMinutes(1));
            this.locales = projectLocales(configuration, project);
            this.countryCodes = projectCountries(configuration, project);
            this.currencies = projectCurrencies(configuration, project);
            LOGGER.debug("Project Languages {}, Countries {}, Currencies {}", locales, countryCodes, currencies);
        } catch (SphereTimeoutException e) {
            throw new RuntimeException("Could not fetch project information", e);
        }
    }

    @Override
    public List<Locale> locales() {
        return locales;
    }

    @Override
    public List<CountryCode> countries() {
        return countryCodes;
    }

    @Override
    public List<CurrencyUnit> currencies() {
        return currencies;
    }

    private static List<Locale> projectLocales(final Configuration configuration, final Project project) {
        return getValues(configuration, CONFIG_LANGUAGES, Locale::forLanguageTag, project.getLanguageLocales());
    }

    private static List<CountryCode> projectCountries(final Configuration configuration, final Project project) {
        return getValues(configuration, CONFIG_COUNTRIES, CountryCode::getByCode, project.getCountries());
    }

    private static List<CurrencyUnit> projectCurrencies(final Configuration configuration, final Project project) {
        return getValues(configuration, CONFIG_CURRENCIES, Monetary::getCurrency, project.getCurrencyUnits());
    }

    private static <T> List<T> getValues(final Configuration configuration, final String configKey,
                                         final Function<String, T> mapper, final List<T> fallbackValues) {
        final List<T> valuesFromConfig = configuration.getStringList(configKey, emptyList()).stream()
                .map(mapper)
                .collect(toList());
        final List<T> values = valuesFromConfig.isEmpty() ? fallbackValues : valuesFromConfig;
        if (values.isEmpty()) {
            throw new SunriseConfigurationException("No configuration defined and CTP project information was empty", configKey);
        }
        return values;
    }
}
