package com.commercetools.sunrise.core.viewmodels.formatters;

import com.commercetools.sunrise.core.i18n.I18nResolver;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
final class ErrorFormatterImpl implements ErrorFormatter {

    private final I18nResolver i18nResolver;

    @Inject
    ErrorFormatterImpl(final I18nResolver i18nResolver) {
        this.i18nResolver = i18nResolver;
    }

    @Override
    public String format(final String messageKey) {
        return i18nResolver.getOrKey(messageKey);
    }
}
