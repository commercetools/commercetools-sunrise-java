package inject;

import com.google.inject.Provider;
import com.neovisionaries.i18n.CountryCode;
import common.contexts.ProjectContext;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.projects.Project;
import io.sphere.sdk.projects.queries.ProjectGet;
import play.Configuration;
import play.Logger;

import javax.inject.Inject;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

class ProjectContextProvider implements Provider<ProjectContext> {
    private static final String CONFIG_LANGUAGES = "application.i18n.languages";
    private static final String CONFIG_COUNTRIES = "application.countries";
    private static final String CONFIG_CURRENCIES = "application.currencies";
    private final Configuration configuration;
    private final SphereClient client;

    @Inject
    private ProjectContextProvider(final Configuration configuration, final SphereClient client) {
        this.configuration = configuration;
        this.client = client;
    }

    @Override
    public ProjectContext get() {
        try {
            final Project project = client.execute(ProjectGet.of()).toCompletableFuture().get();
            final List<Locale> languages = getLanguages(project);
            final List<CountryCode> countries = getCountries(project);
            final List<CurrencyUnit> currencies = getCurrencies(project);
            Logger.debug("Provide ProjectContext:"
                    + " Languages " + languages + ","
                    + " Countries " + countries + ","
                    + " Currencies " + currencies);
            return ProjectContext.of(languages, countries, currencies);
        } catch (ExecutionException | InterruptedException e) {
            throw new SunriseInitializationException("Could not fetch project information", e);
        }
    }

    private List<Locale> getLanguages(final Project project) {
        final List<Locale> locales = configuration.getStringList(CONFIG_LANGUAGES, project.getLanguages())
                .stream()
                .map(Locale::forLanguageTag)
                .collect(toList());
        if (locales.isEmpty()) {
            throw new SunriseInitializationException("No language defined, neither in project nor in configuration '" + CONFIG_LANGUAGES + "'");
        }
        return locales;
    }

    private List<CountryCode> getCountries(final Project project) {
        final List<CountryCode> countriesFromConfig = getCountriesFromConfig();
        final List<CountryCode> countries = countriesFromConfig.isEmpty() ? project.getCountries() : countriesFromConfig;
        if (countries.isEmpty()) {
            throw new SunriseInitializationException("No country defined, neither in project nor in configuration '" + CONFIG_COUNTRIES + "'");
        }
        return countries;
    }

    private List<CurrencyUnit> getCurrencies(final Project project) {
        final List<CurrencyUnit> currencies = configuration.getStringList(CONFIG_CURRENCIES, emptyList())
                .stream()
                .map(Monetary::getCurrency)
                .collect(toList());
        // TODO Fallback to CTP currencies
        if (currencies.isEmpty()) {
            throw new SunriseInitializationException("No currency defined in configuration '" + CONFIG_CURRENCIES + "'");
        }
        return currencies;
    }

    private List<CountryCode> getCountriesFromConfig() {
        return configuration.getStringList(CONFIG_COUNTRIES, emptyList()).stream()
                .map(CountryCode::valueOf)
                .collect(toList());
    }
}
