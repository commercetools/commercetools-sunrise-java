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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

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

    @Inject
    private UserContextImpl(final Locale locale, final CountryCode country, final CurrencyUnit currency,
                            final Http.Context context, final ProjectContext projectContext) {
        this.locales = acceptedLocales(context, projectContext, locale);
        this.country = country;
        this.currency = currency;
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

    private static List<Locale> acceptedLocales(final Http.Context context, final ProjectContext projectContext, final Locale locale) {
        final List<Locale> acceptedLocales = new ArrayList<>();
        acceptedLocales.add(locale);
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

}
