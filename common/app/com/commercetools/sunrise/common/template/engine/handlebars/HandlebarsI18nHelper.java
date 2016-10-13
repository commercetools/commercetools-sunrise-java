package com.commercetools.sunrise.common.template.engine.handlebars;

import com.commercetools.sunrise.common.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifierFactory;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import io.sphere.sdk.models.Base;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

final class HandlebarsI18nHelper extends Base implements Helper<String> {

    static final String LANGUAGE_TAGS_IN_CONTEXT_KEY = "context-language-tags";
    private final I18nResolver i18n;
    private final I18nIdentifierFactory i18nIdentifierFactory;

    HandlebarsI18nHelper(final I18nResolver i18n, final I18nIdentifierFactory i18nIdentifierFactory) {
        this.i18n = i18n;
        this.i18nIdentifierFactory = i18nIdentifierFactory;
    }

    @Override
    public CharSequence apply(final String context, final Options options) throws IOException {
        final List<Locale> locales = getLocalesFromContext(options.context);
        final I18nIdentifier i18nIdentifier = i18nIdentifierFactory.create(context);
        return i18n.getOrEmpty(locales, i18nIdentifier, options.hash);
    }

    @SuppressWarnings("unchecked")
    private static List<Locale> getLocalesFromContext(final Context context) {
        final Object languageTagsAsObject = context.get(LANGUAGE_TAGS_IN_CONTEXT_KEY);
        if (languageTagsAsObject instanceof List) {
            final List<String> locales = (List<String>) languageTagsAsObject;
            return locales.stream()
                    .map(Locale::forLanguageTag)
                    .collect(toList());
        } else {
            return emptyList();
        }
    }
}
