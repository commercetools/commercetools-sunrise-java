package com.commercetools.sunrise.common.utils;

import com.commercetools.sunrise.common.contexts.ProjectContext;
import com.commercetools.sunrise.common.contexts.RequestScoped;
import io.sphere.sdk.models.LocalizedString;
import play.mvc.Http;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RequestScoped
class LocalizedStringResolverImpl implements LocalizedStringResolver {

    private final List<Locale> acceptedLocales;

    @Inject
    LocalizedStringResolverImpl(final Locale locale, final Http.Context httpContext, final ProjectContext projectContext) {
        this.acceptedLocales = acceptedLocales(locale, httpContext, projectContext);
    }

    @Override
    public Optional<String> find(@NotNull final LocalizedString localizedString) {
        return localizedString.find(acceptedLocales);
    }

    private static List<Locale> acceptedLocales(final Locale locale, final Http.Context httpContext,
                                                final ProjectContext projectContext) {
        final ArrayList<Locale> acceptedLocales = new ArrayList<>();
        acceptedLocales.add(locale);
        acceptedLocales.addAll(requestAcceptedLanguages(httpContext, projectContext));
        return acceptedLocales.stream()
                .distinct()
                .collect(toList());
    }

    private static List<Locale> requestAcceptedLanguages(final Http.Context httpContext, final ProjectContext projectContext) {
        return httpContext.request().acceptLanguages().stream()
                .map(lang -> Locale.forLanguageTag(lang.code()))
                .filter(projectContext::isLocaleSupported)
                .collect(toList());
    }
}
