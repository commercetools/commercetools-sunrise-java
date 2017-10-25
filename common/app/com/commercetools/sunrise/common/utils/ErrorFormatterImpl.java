package com.commercetools.sunrise.common.utils;

import com.commercetools.sunrise.common.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifierFactory;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;

import javax.inject.Inject;
import java.util.List;
import java.util.Locale;
import java.util.Map;

class ErrorFormatterImpl implements ErrorFormatter {

    private final I18nResolver i18nResolver;
    private final I18nIdentifierFactory i18nIdentifierFactory;

    @Inject
    ErrorFormatterImpl(final I18nResolver i18nResolver, final I18nIdentifierFactory i18nIdentifierFactory) {
        this.i18nResolver = i18nResolver;
        this.i18nIdentifierFactory = i18nIdentifierFactory;
    }

    @Override
    public String format(final List<Locale> locales, final String messageKey, final Map<String, Object> args) {
        final I18nIdentifier i18nIdentifier = i18nIdentifierFactory.create(messageKey);
        return i18nResolver.getOrKey(locales, i18nIdentifier, args);
    }
}
