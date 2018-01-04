package com.commercetools.sunrise.core.sessions;

import io.sphere.sdk.models.Resource;
import play.Configuration;

import javax.annotation.Nullable;
import java.util.Optional;

public abstract class AbstractResourceInSession<T extends Resource<T>> implements ResourceInSession<T> {

    private final Configuration configuration;
    private final StoringStrategy storingStrategy;
    private final String cookieName;
    private final boolean cookieSecure;
    private final boolean cookieHttpOnly;
    private final String versionSessionKey;

    protected AbstractResourceInSession(final Configuration config, final StoringStrategy storingStrategy) {
        this.configuration = config;
        this.cookieName = config.getString("cookieName");
        this.cookieSecure = config.getBoolean("cookieSecure");
        this.cookieHttpOnly = config.getBoolean("cookieHttpOnly");
        this.versionSessionKey = config.getString("versionSessionKey");
        this.storingStrategy = storingStrategy;
    }

    protected final Configuration getConfiguration() {
        return configuration;
    }

    protected final StoringStrategy getStoringStrategy() {
        return storingStrategy;
    }

    @Override
    public Optional<String> findId() {
        return storingStrategy.findInCookies(cookieName);
    }

    @Override
    public Optional<Long> findVersion() {
        return storingStrategy.findInSession(versionSessionKey)
                .flatMap(version -> {
                    try {
                        return Optional.of(Long.valueOf(version));
                    } catch (NumberFormatException e) {
                        return Optional.empty();
                    }
                });
    }

    @Override
    public void store(@Nullable final T resource) {
        storeId(resource);
        storeVersion(resource);
    }

    private void storeId(final @Nullable T resource) {
        final String id = resource != null ? resource.getId() : null;
        storingStrategy.overwriteInCookies(cookieName, id, cookieHttpOnly, cookieSecure);
    }

    private void storeVersion(final @Nullable T resource) {
        final Long version = resource != null ? resource.getVersion() : null;
        storingStrategy.overwriteInSession(versionSessionKey, String.valueOf(version));
    }

    @Override
    public void remove() {
        storingStrategy.removeFromCookies(cookieName);
        storingStrategy.removeFromSession(versionSessionKey);
    }
}
