package com.commercetools.sunrise.framework.localization;

import com.commercetools.sunrise.ctp.project.ProjectContext;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import io.sphere.sdk.models.Base;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.Http;

import javax.inject.Inject;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;
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
    UserLanguageImpl(final Locale locale, final ProjectContext projectContext) {
        this.locale = locale;
        this.locales = acceptedLocales(locale, projectContext);
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

    private static List<Locale> acceptedLocales(final Locale locale, final ProjectContext projectContext) {
        final Http.Context httpContext = Http.Context.current.get();
        if (httpContext != null) {
            return Stream.concat(Stream.of(locale), requestAcceptedLanguages(httpContext.request(), projectContext))
                    .distinct()
                    .collect(toList());
        } else {
            return singletonList(locale);
        }
    }

    private static Stream<Locale> requestAcceptedLanguages(final Http.Request httpRequest, final ProjectContext projectContext) {
        return httpRequest.acceptLanguages().stream()
                .map(lang -> Locale.forLanguageTag(lang.code()))
                .filter(projectContext::isLocaleSupported);
    }

}
