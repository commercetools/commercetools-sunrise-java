package com.commercetools.sunrise.common.utils;

import com.commercetools.sunrise.contexts.ProjectContext;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.framework.template.i18n.I18nIdentifierFactory;
import com.commercetools.sunrise.framework.template.i18n.I18nResolver;
import play.mvc.Http;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static com.commercetools.sunrise.common.utils.LanguageUtils.requestAcceptedLanguages;
import static java.util.stream.Collectors.toList;

@RequestScoped
final class PageTitleResolverImpl implements PageTitleResolver {

    private final List<Locale> availableLocales;
    private final I18nResolver i18nResolver;
    private final I18nIdentifierFactory i18nIdentifierFactory;

    @Inject
    PageTitleResolverImpl(final Locale locale, final Http.Context httpContext, final ProjectContext projectContext,
                                 final I18nResolver i18nResolver, final I18nIdentifierFactory i18nIdentifierFactory) {
        this.availableLocales = getAvailableLocales(locale, httpContext, projectContext);
        this.i18nResolver = i18nResolver;
        this.i18nIdentifierFactory = i18nIdentifierFactory;
    }

    @Override
    public Optional<String> find(final String key) {
        final I18nIdentifier i18nIdentifier = i18nIdentifierFactory.create(key);
        return i18nResolver.get(availableLocales, i18nIdentifier);
    }

    private static List<Locale> getAvailableLocales(final Locale locale, final Http.Context httpContext, final ProjectContext projectContext) {
        final List<Locale> acceptedLocales = new ArrayList<>();
        acceptedLocales.add(locale);
        acceptedLocales.addAll(requestAcceptedLanguages(httpContext, projectContext));
        return acceptedLocales.stream()
                .distinct()
                .collect(toList());
    }
}
