package com.commercetools.sunrise.common.template.engine.handlebars;

import com.commercetools.sunrise.common.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifierFactory;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import io.sphere.sdk.models.Base;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.commercetools.sunrise.common.template.engine.handlebars.HandlebarsHelperUtils.getLocalesFromContext;

final class HandlebarsI18nHelper extends Base implements Helper<String> {

    private final I18nResolver i18n;
    private final I18nIdentifierFactory i18nIdentifierFactory;

    public HandlebarsI18nHelper(final I18nResolver i18n, final I18nIdentifierFactory i18nIdentifierFactory) {
        this.i18n = i18n;
        this.i18nIdentifierFactory = i18nIdentifierFactory;
    }

    @Override
    public CharSequence apply(final String context, final Options options) throws IOException {
        final List<Locale> locales = getLocalesFromContext(options.context);
        final I18nIdentifier i18nIdentifier = i18nIdentifierFactory.create(context);
        return i18n.getOrEmpty(locales, i18nIdentifier, options.hash);
    }
}
