package com.commercetools.sunrise.framework.localization;

import play.mvc.Http;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static java.util.Arrays.asList;

public final class LocaleFromUrlProvider implements Provider<Locale> {

    private final ProjectContext projectContext;

    @Inject
    public LocaleFromUrlProvider(final ProjectContext projectContext) {
        this.projectContext = projectContext;
    }

    @Override
    public Locale get() {
        return findCurrentLanguage()
                .filter(projectContext::isLocaleSupported)
                .orElseGet(projectContext::defaultLocale);
    }

    private Optional<Locale> findCurrentLanguage() {
        final Http.Context httpContext = Http.Context.current.get();
        if (httpContext != null) {
            return indexOfLanguageTagInRoutePattern(httpContext)
                    .map(index -> {
                        final String languageTag = httpContext.request().path().split("/")[index];
                        return Locale.forLanguageTag(languageTag);
                    });
        } else {
            return Optional.empty();
        }
    }

    private Optional<Integer> indexOfLanguageTagInRoutePattern(final Http.Context httpContext) {
        return Optional.ofNullable(httpContext.args.get("ROUTE_PATTERN"))
                .map(routePattern -> routePattern.toString().replaceAll("<[^>]+>", "")) // Remove regex because splitting '$languageTag<[^/]+>' with '/' would create more words
                .map(routePattern -> {
                    final List<String> paths = asList(routePattern.split("/"));
                    return paths.indexOf("$languageTag");
                })
                .filter(index -> index >= 0);
    }
}
