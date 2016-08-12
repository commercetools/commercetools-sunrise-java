package com.commercetools.sunrise.common.contexts;

import com.commercetools.sunrise.common.SunriseConfigurationException;
import com.google.inject.Provider;
import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.client.SphereRequest;
import io.sphere.sdk.client.SphereTimeoutException;
import io.sphere.sdk.projects.Project;
import io.sphere.sdk.projects.queries.ProjectGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Configuration;

import javax.inject.Inject;
import javax.inject.Named;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.time.Duration;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

import static io.sphere.sdk.client.SphereClientUtils.blockingWait;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public final class ProjectContextProvider implements Provider<ProjectContext> {

    private static final Logger logger = LoggerFactory.getLogger(ProjectContextProvider.class);
    private static final String CONFIG_LANGUAGES = "application.i18n.languages";
    private static final String CONFIG_COUNTRIES = "application.countries";
    private static final String CONFIG_CURRENCIES = "application.currencies";
    @Inject
    private Configuration configuration;
    @Inject
    @Named("global")
    private SphereClient client;

    @Override
    public ProjectContext get() {
        try {
            final SphereRequest<Project> request = ProjectGet.of();
            final Project project = blockingWait(client.execute(request), Duration.ofMinutes(1));
            final List<Locale> languages = getLanguages(project);
            final List<CountryCode> countries = getCountries(project);
            final List<CurrencyUnit> currencies = getCurrencies(project);
            logger.info("Provide ProjectContext:"
                    + " Languages " + languages + ","
                    + " Countries " + countries + ","
                    + " Currencies " + currencies);
            return ProjectContextImpl.of(languages, countries, currencies);
        } catch (SphereTimeoutException e) {
            throw new RuntimeException("Could not fetch project information", e);
        }
    }

    private List<Locale> getLanguages(final Project project) {
        return getValues(CONFIG_LANGUAGES, Locale::forLanguageTag, project.getLanguageLocales());
    }

    private List<CountryCode> getCountries(final Project project) {
        return getValues(CONFIG_COUNTRIES, CountryCode::getByCode, project.getCountries());
    }

    private List<CurrencyUnit> getCurrencies(final Project project) {
        return getValues(CONFIG_CURRENCIES, Monetary::getCurrency, project.getCurrencyUnits());
    }

    private <T> List<T> getValues(final String configKey, final Function<String, T> mapper, final List<T> fallbackValues) {
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
