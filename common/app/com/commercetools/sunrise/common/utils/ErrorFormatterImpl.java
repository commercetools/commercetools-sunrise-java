package com.commercetools.sunrise.common.utils;

import com.commercetools.sunrise.contexts.UserLanguage;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.framework.template.i18n.I18nIdentifierFactory;
import com.commercetools.sunrise.framework.template.i18n.I18nResolver;

import javax.inject.Inject;

@RequestScoped
final class ErrorFormatterImpl implements ErrorFormatter {

    private final UserLanguage userLanguage;
    private final I18nResolver i18nResolver;
    private final I18nIdentifierFactory i18nIdentifierFactory;

    @Inject
    ErrorFormatterImpl(final UserLanguage userLanguage, final I18nResolver i18nResolver, final I18nIdentifierFactory i18nIdentifierFactory) {
        this.userLanguage = userLanguage;
        this.i18nResolver = i18nResolver;
        this.i18nIdentifierFactory = i18nIdentifierFactory;
    }

    @Override
    public String format(final String messageKey) {
        final I18nIdentifier i18nIdentifier = i18nIdentifierFactory.create(messageKey);
        return i18nResolver.getOrKey(userLanguage.locales(), i18nIdentifier);
    }
}
