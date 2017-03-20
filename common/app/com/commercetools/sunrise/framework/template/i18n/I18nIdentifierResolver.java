package com.commercetools.sunrise.framework.template.i18n;

import com.google.inject.ImplementedBy;

import java.util.Optional;

@ImplementedBy(I18nIdentifierResolverImpl.class)
@FunctionalInterface
public interface I18nIdentifierResolver {

    Optional<String> resolve(final String i18nIdentifier);

    default String resolveOrEmpty(final String i18nIdentifier) {
        return resolve(i18nIdentifier).orElse("");
    }

    default String resolveOrKey(final String i18nIdentifier) {
        return resolve(i18nIdentifier).orElse(i18nIdentifier);
    }
}
