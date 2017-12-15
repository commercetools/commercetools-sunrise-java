package com.commercetools.sunrise.core.viewmodels;

import com.commercetools.sunrise.core.i18n.I18nResolver;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
final class PageTitleResolverImpl implements PageTitleResolver {

    private final I18nResolver i18nResolver;

    @Inject
    PageTitleResolverImpl(final I18nResolver i18nResolver) {
        this.i18nResolver = i18nResolver;
    }

    @Override
    public Optional<String> find(final String key) {
        return i18nResolver.get(key);
    }
}
