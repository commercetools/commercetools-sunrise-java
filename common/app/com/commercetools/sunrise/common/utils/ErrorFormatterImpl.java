package com.commercetools.sunrise.common.utils;

import com.commercetools.sunrise.common.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifierFactory;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;

import javax.inject.Inject;
import java.util.List;
import java.util.Locale;

class ErrorFormatterImpl implements ErrorFormatter {

    @Inject
    private I18nResolver i18nResolver;
    @Inject
    private I18nIdentifierFactory i18nIdentifierFactory;

    @Override
    public String format(final List<Locale> locales, final String messageKey) {
        final I18nIdentifier i18nIdentifier = i18nIdentifierFactory.create(messageKey);
        return i18nResolver.getOrKey(locales, i18nIdentifier);
    }
}
