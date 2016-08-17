package com.commercetools.sunrise.common.contexts;

import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.channels.Channel;
import io.sphere.sdk.customergroups.CustomerGroup;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.models.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.Http;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static com.commercetools.sunrise.common.localization.SunriseLocalizationController.SESSION_COUNTRY;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

/**
 * Extracts the selected language from the URL and completes the list of available locales with those received
 * in the AcceptedLanguage HTTP header. The country is obtained from the session and the currency corresponds
 * to the selected country.
 *
 * Any value not supported by the {@link ProjectContext} or duplicated is discarded. In the case of selected values,
 * these are replaced by the default values for the project.
 */

@RequestScoped
final class UserContextImpl extends Base implements UserContext {

    private static final Logger logger = LoggerFactory.getLogger(UserContext.class);
    private final List<Locale> locales;
    private final CountryCode country;
    private final CurrencyUnit currency;
    @Nullable
    private final Reference<CustomerGroup> customerGroup;
    @Nullable
    private final Reference<Channel> channel;

    UserContextImpl(final List<Locale> locales, final CountryCode country, final CurrencyUnit currency,
                    @Nullable final Reference<CustomerGroup> customerGroup, @Nullable final Reference<Channel> channel) {
        this.locales = locales;
        this.country = country;
        this.currency = currency;
        this.customerGroup = customerGroup;
        this.channel = channel;
        if (locales.isEmpty() || locales.get(0) == null) {
            throw new IllegalArgumentException("Locales must contain at least one valid locale.");
        }
    }

    @Inject
    private UserContextImpl(final Http.Context context, final ProjectContext projectContext) {
        this.locales = acceptedLocales(context, projectContext);
        this.country = userCountry(context, projectContext);
        this.currency = userCurrency(context, projectContext, country);
        this.customerGroup = null;
        this.channel = null;
        logger.debug("Provided UserContext: Languages {}, Country {}, Currency {}", locales, country, currency);
    }

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
        return Optional.ofNullable(customerGroup);
    }

    @Override
    public Optional<Reference<Channel>> channel() {
        return Optional.ofNullable(channel);
    }

    private static List<Locale> acceptedLocales(final Http.Context context, final ProjectContext projectContext) {
        final ArrayList<Locale> acceptedLocales = new ArrayList<>();
        acceptedLocales.add(userLanguage(context, projectContext));
        acceptedLocales.addAll(requestAcceptedLanguages(context, projectContext));
        return acceptedLocales.stream()
                .distinct()
                .collect(toList());
    }

    private static List<Locale> requestAcceptedLanguages(final Http.Context context, final ProjectContext projectContext) {
        return context.request().acceptLanguages().stream()
                .map(lang -> Locale.forLanguageTag(lang.code()))
                .filter(projectContext::isLocaleSupported)
                .collect(toList());
    }

    private static Optional<CountryCode> findCurrentCountry(final Http.Context context) {
        return Optional.ofNullable(context.session().get(SESSION_COUNTRY))
                .map(countryInSession -> CountryCode.getByCode(countryInSession, false));
    }

    private static Optional<CurrencyUnit> findCurrentCurrency(final Http.Context context, final CountryCode currentCountry) {
        return Optional.ofNullable(currentCountry.getCurrency())
                .map(countryCurrency -> Monetary.getCurrency(countryCurrency.getCurrencyCode()));
    }

    private static Optional<Locale> findCurrentLanguage(final Http.Context context) {
        return indexOfLanguageTagInRoutePattern(context)
                .map(index -> {
                    final String languageTag = context.request().path().split("/")[index];
                    return Locale.forLanguageTag(languageTag);
                });
    }

    private static Locale userLanguage(final Http.Context context, final ProjectContext projectContext) {
        return findCurrentLanguage(context)
                .filter(projectContext::isLocaleSupported)
                .orElseGet(projectContext::defaultLocale);
    }

    private static CountryCode userCountry(final Http.Context context, final ProjectContext projectContext) {
        return findCurrentCountry(context)
                .filter(projectContext::isCountrySupported)
                .orElseGet(projectContext::defaultCountry);
    }

    private static CurrencyUnit userCurrency(final Http.Context context, final ProjectContext projectContext, final CountryCode currentCountry) {
        return findCurrentCurrency(context, currentCountry)
                .filter(projectContext::isCurrencySupported)
                .orElseGet(projectContext::defaultCurrency);
    }

    private static Optional<Integer> indexOfLanguageTagInRoutePattern(final Http.Context context) {
        return Optional.ofNullable(context.args.get("ROUTE_PATTERN"))
                .map(routePattern -> routePattern.toString().replaceAll("<[^>]+>", "")) //hack since splitting '$languageTag<[^/]+>' with '/' would create more words
                .map(routePattern -> {
                    final List<String> paths = asList(routePattern.split("/"));
                    return paths.indexOf("$languageTag");
                })
                .filter(index -> index >= 0);
    }

}
