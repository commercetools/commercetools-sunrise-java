package com.commercetools.sunrise.framework.viewmodels;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.template.i18n.I18nIdentifierResolver;

import javax.inject.Inject;
import java.util.Optional;

@RequestScoped
final class PageTitleResolverImpl implements PageTitleResolver {

    private final I18nIdentifierResolver i18nIdentifierResolver;

    @Inject
    PageTitleResolverImpl(final I18nIdentifierResolver i18nIdentifierResolver) {
        this.i18nIdentifierResolver = i18nIdentifierResolver;
    }

    @Override
    public Optional<String> find(final String key) {
        return i18nIdentifierResolver.resolve(key);
    }
}
