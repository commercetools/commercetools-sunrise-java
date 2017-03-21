package com.commercetools.sunrise.framework.localization;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import io.sphere.sdk.models.Base;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.Http;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static java.util.stream.Collectors.toList;

/**
 * Provides a distinct list with the selected {@link Locale} and the those coming from the AcceptedLanguage HTTP header.
 * Languages not supported by the {@link ProjectContext} are discarded.
 */
@RequestScoped
final class UserLanguageImpl extends Base implements UserLanguage {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserLanguage.class);

    private final Locale locale;
    private final List<Locale> locales;

    @Inject
    UserLanguageImpl(final Locale locale, final Http.Context httpContext, final ProjectContext projectContext) {
        this.locale = locale;
        this.locales = acceptedLocales(locale, httpContext.request(), projectContext);
        LOGGER.debug("User locale {}, locales {}", locale, locales);
    }

    @Override
    public Locale locale() {
        return locale;
    }

    @Override
    public List<Locale> locales() {
        return locales;
    }

    private static List<Locale> acceptedLocales(final Locale locale, final Http.Request request, final ProjectContext projectContext) {
        final List<Locale> acceptedLocales = new ArrayList<>();
        acceptedLocales.add(locale);
        acceptedLocales.addAll(requestAcceptedLanguages(request, projectContext));
        return acceptedLocales.stream()
                .distinct()
                .collect(toList());
    }

    private static List<Locale> requestAcceptedLanguages(final Http.Request request, final ProjectContext projectContext) {
        return request.acceptLanguages().stream()
                .map(lang -> Locale.forLanguageTag(lang.code()))
                .filter(projectContext::isLocaleSupported)
                .collect(toList());
    }

}
