package com.commercetools.sunrise.framework.viewmodels;

import com.google.inject.ImplementedBy;

import java.util.Optional;

@ImplementedBy(PageTitleResolverImpl.class)
@FunctionalInterface
public interface PageTitleResolver {

    Optional<String> find(final String key);

    default String getOrEmpty(final String key) {
        return find(key).orElse("");
    }
}
