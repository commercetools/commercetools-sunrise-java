package com.commercetools.sunrise.framework.template.i18n;

import com.commercetools.sunrise.framework.localization.UserLanguage;
import com.commercetools.sunrise.framework.injection.RequestScoped;

import javax.inject.Inject;
import java.util.Optional;

@RequestScoped
final class I18nIdentifierResolverImpl implements I18nIdentifierResolver {

    private final UserLanguage userLanguage;
    private final I18nIdentifierFactory i18nIdentifierFactory;
    private final I18nResolver i18nResolver;

    @Inject
    I18nIdentifierResolverImpl(final UserLanguage userLanguage, final I18nIdentifierFactory i18nIdentifierFactory, final I18nResolver i18nResolver) {
        this.userLanguage = userLanguage;
        this.i18nIdentifierFactory = i18nIdentifierFactory;
        this.i18nResolver = i18nResolver;
    }

    @Override
    public Optional<String> resolve(final String i18nIdentifier) {
        return i18nResolver.get(userLanguage.locales(), i18nIdentifierFactory.create(i18nIdentifier));
    }
}
