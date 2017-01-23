package com.commercetools.sunrise.common.utils;

import com.commercetools.sunrise.common.contexts.ProjectContext;
import play.mvc.Http;

import java.util.List;
import java.util.Locale;

import static java.util.stream.Collectors.toList;

public final class LanguageUtils {

    private LanguageUtils() {
    }

    public static List<Locale> requestAcceptedLanguages(final Http.Context httpContext, final ProjectContext projectContext) {
        return httpContext.request().acceptLanguages().stream()
                .map(lang -> Locale.forLanguageTag(lang.code()))
                .filter(projectContext::isLocaleSupported)
                .collect(toList());
    }
}
