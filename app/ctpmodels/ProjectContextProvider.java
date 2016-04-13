package ctpmodels;

import com.google.inject.Provider;
import com.neovisionaries.i18n.CountryCode;
import common.contexts.ProjectContext;
import common.SunriseInitializationException;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.projects.Project;
import io.sphere.sdk.projects.queries.ProjectGet;
import play.Configuration;
import play.Logger;

import javax.inject.Inject;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

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
            Logger.info("Provide ProjectContext:"
                    + " Languages " + languages + ","
                    + " Countries " + countries + ","
                    + " Currencies " + currencies);
            return ProjectContext.of(languages, countries, currencies);
        } catch (ExecutionException | InterruptedException e) {
            throw new SunriseInitializationException("Could not fetch project information", e);
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
            throw new SunriseInitializationException("No '" + configKey + "' defined and CTP project information was empty");
        }
        return values;
    }
}
