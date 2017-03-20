package com.commercetools.sunrise.framework.viewmodels.formatters;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.template.i18n.I18nIdentifierResolver;

import javax.inject.Inject;

@RequestScoped
final class ErrorFormatterImpl implements ErrorFormatter {

    private final I18nIdentifierResolver i18nIdentifierResolver;

    @Inject
    ErrorFormatterImpl(final I18nIdentifierResolver i18nIdentifierResolver) {
        this.i18nIdentifierResolver = i18nIdentifierResolver;
    }

    @Override
    public String format(final String messageKey) {
        return i18nIdentifierResolver.resolveOrKey(messageKey);
    }
}
