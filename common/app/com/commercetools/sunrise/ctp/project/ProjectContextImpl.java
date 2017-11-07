package com.commercetools.sunrise.ctp.project;

import com.commercetools.sunrise.play.configuration.SunriseConfigurationException;
import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.projects.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Configuration;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

/**
 * Uses the associated commercetools platform project to obtain the supported languages, countries and currencies.
 * Nevertheless it enables the possibility to override these values via configuration.
 */

@Singleton
final class ProjectContextImpl extends Base implements ProjectContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectContext.class);

    private final List<Locale> locales;
    private final List<CountryCode> countryCodes;
    private final List<CurrencyUnit> currencies;

    @Inject
    ProjectContextImpl(final Configuration globalConfig, final Project project) {
        this(globalConfig, "sunrise.ctp.project", project);
    }

    ProjectContextImpl(final Configuration globalConfig, final String configPath, final Project project) {
        final Configuration config = globalConfig.getConfig(configPath);
        this.locales = projectLocales(config, project);
        this.countryCodes = projectCountries(config, project);
        this.currencies = projectCurrencies(config, project);
        LOGGER.debug("Initialized ProjectContext: Languages {}, Countries {}, Currencies {}", locales, countryCodes, currencies);
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
        return getValues(configuration, "languages", Locale::forLanguageTag, project.getLanguageLocales());
    }

    private static List<CountryCode> projectCountries(final Configuration configuration, final Project project) {
        return getValues(configuration, "countries", CountryCode::getByCode, project.getCountries());
    }

    private static List<CurrencyUnit> projectCurrencies(final Configuration configuration, final Project project) {
        return getValues(configuration, "currencies", Monetary::getCurrency, project.getCurrencyUnits());
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