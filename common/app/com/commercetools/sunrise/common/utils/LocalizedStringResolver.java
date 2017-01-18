package com.commercetools.sunrise.common.utils;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.models.LocalizedString;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@ImplementedBy(LocalizedStringResolverImpl.class)
@FunctionalInterface
public interface LocalizedStringResolver {

    Optional<String> find(@NotNull final LocalizedString localizedString);

    default String getOrEmpty(@NotNull final LocalizedString localizedString) {
        return find(localizedString).orElse("");
    }
}
