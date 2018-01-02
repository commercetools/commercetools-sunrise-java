package com.commercetools.sunrise.core.sessions;

import play.Configuration;
import play.cache.CacheApi;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;
import java.util.UUID;

/**
 * Uses a session cookie to store string information about the user.
 * Objects are first saved into the cache and uniquely associated with the user's session cookie with a generated session ID.
 * For more information: <a href="https://www.playframework.com/documentation/2.5.x/JavaSessionFlash">Session in Play Framework</a>
 */
@Singleton
public final class CachedStoringStrategy extends AbstractStoringStrategy {

    private final String sessionIdKey;
    private final CacheApi cacheApi;

    @Inject
    CachedStoringStrategy(final Configuration configuration, final CacheApi cacheApi) {
        this.sessionIdKey = configuration.getString("play.http.session.idKey");
        this.cacheApi = cacheApi;
    }

    @Override
    public <U> Optional<U> findInSession(final String key, final Class<U> clazz) {
        return findInSession(key).flatMap(cacheKey -> findInCache(cacheKey, clazz));
    }

    @Override
    public <U> void overwriteInSession(final String key, @Nullable final U obj) {
        final String cacheKey = getCacheKeyInSession(key);
        storeInCache(cacheKey, obj);
        overwriteInSession(key, cacheKey);
    }

    @Override
    public <U> Optional<U> findInCookies(final String name, final Class<U> clazz) {
        return findInCookies(name).flatMap(cacheKey -> findInCache(cacheKey, clazz));
    }

    @Override
    public <U> void overwriteInCookies(final String name, @Nullable final U obj,
                                       final boolean httpOnly, final boolean secure) {

        final String cacheKey = getCacheKeyInCookies(name);
        storeInCache(cacheKey, obj);
        overwriteInCookies(name, cacheKey, httpOnly, secure);
    }

    private <U> Optional<U> findInCache(final String cacheKey, final Class<U> clazz) {
        return Optional.ofNullable(cacheApi.get(cacheKey))
                .flatMap(objectWithoutType -> tryCastingToType(objectWithoutType, clazz, cacheKey));
    }

    private <U> void storeInCache(final String cacheKey, final @Nullable U obj) {
        if (obj != null) {
            cacheApi.set(cacheKey, obj);
            LOGGER.debug("Stored object in cache with key \"{}\"");
        }
    }

    private <U> Optional<U> tryCastingToType(final Object obj, final Class<U> clazz, final String cacheKey) {
        try {
            return Optional.of(clazz.cast(obj));
        } catch (ClassCastException e) {
            LOGGER.error("Could not cast value in cache key \"{}\" into type \"{}\"", cacheKey, clazz.getSimpleName(), e);
            return Optional.empty();
        }
    }

    /**
     * Obtains the cache key for that session key or generates a unique key for that session if not present.
     * @param key the session key
     * @return the cache key associated with that session key
     */
    private String getCacheKeyInSession(final String key) {
        return findInSession(key).orElseGet(() -> getSessionId() + key);
    }

    /**
     * Obtains the cache key for that cookie name or generates a unique key for that cookie if not present.
     * @param name the cookie name
     * @return the cache key associated with that cookie name
     */
    private String getCacheKeyInCookies(final String name) {
        return findInCookies(name).orElseGet(() -> getSessionId() + name);
    }

    /**
     * Obtains the session ID or generates a new one if not present.
     * @return the ID associated with that session
     */
    private String getSessionId() {
        return findInSession(sessionIdKey).orElseGet(() -> {
            final String uuid = generateSessionId();
            overwriteInSession(sessionIdKey, uuid);
            return uuid;
        });
    }

    private String generateSessionId() {
        return UUID.randomUUID().toString();
    }
}
