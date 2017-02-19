package com.commercetools.sunrise.sessions;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import play.Configuration;
import play.cache.CacheApi;
import play.mvc.Http;

import javax.inject.Inject;
import java.util.Optional;
import java.util.UUID;

/**
 * Uses a session cookie to store string information about the user.
 * Objects are first saved into the cache and uniquely associated with the user's session cookie with a generated session ID.
 * For more information: <a href="https://www.playframework.com/documentation/2.5.x/JavaSessionFlash">Session in Play Framework</a>
 */
@RequestScoped
public final class CacheableObjectStoringSessionCookieStrategy extends SessionCookieStrategy implements ObjectStoringSessionStrategy {

    private static final String DEFAULT_SESSION_ID_KEY = "sunrise-session-id";
    private final String sessionIdKey;
    private final CacheApi cacheApi;

    @Inject
    public CacheableObjectStoringSessionCookieStrategy(final Http.Session session, final CacheApi cacheApi, final Configuration configuration) {
        super(session);
        this.cacheApi = cacheApi;
        this.sessionIdKey = configuration.getString("session.idKey", DEFAULT_SESSION_ID_KEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <U> Optional<U> findObjectByKey(final String key, final Class<U> clazz) {
        return findValueByKey(key)
                .flatMap(cacheKey -> {
                    final Optional<U> object = findInCache(cacheKey, clazz);
                    if (object.isPresent()) {
                        logger.debug("Loaded from cache \"{}\" = {}", cacheKey, object);
                    } else {
                        logger.debug("Not found in cache \"{}\"", cacheKey);
                    }
                    return object;
                });
    }

    private <U> Optional<U> findInCache(final String cacheKey, final Class<U> clazz) {
        return Optional.ofNullable(cacheApi.get(cacheKey))
                .flatMap(objectWithoutType -> tryCastingToType(objectWithoutType, clazz, cacheKey));
    }

    private <U> Optional<U> tryCastingToType(final Object object, final Class<U> clazz, final String cacheKey) {
        try {
            return Optional.of(clazz.cast(object));
        } catch (ClassCastException e) {
            logger.error("Could not cast value in cache key \"{}\" into type \"{}\"", cacheKey, clazz.getSimpleName(), e);
            return Optional.empty();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <U> void overwriteObjectByKey(final String key, final U object) {
        final String cacheKey = getCacheKey(key);
        cacheApi.set(cacheKey, object);
        logger.debug("Saved in cache \"{}\" = {}", cacheKey, object);
        overwriteValueByKey(key, cacheKey);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeObjectByKey(final String key) {
        findValueByKey(key)
                .ifPresent(cacheKey -> {
                    cacheApi.remove(cacheKey);
                    logger.debug("Removed from cache \"{}\"", cacheKey);
                    removeValueByKey(key);
                });
    }

    /**
     * Obtains the cache key for that session key or generates a unique key for that session if not present.
     * @param sessionKey the session key
     * @return the cache key associated with that session and session key
     */
    private String getCacheKey(final String sessionKey) {
        return findValueByKey(sessionKey)
                .orElseGet(() -> getSessionId() + sessionKey);
    }

    /**
     * Obtains the session ID or generates a new one if not present.
     * @return the ID associated with that session
     */
    private String getSessionId() {
        return findValueByKey(sessionIdKey)
                .orElseGet(() -> {
                    final String uuid = UUID.randomUUID().toString();
                    overwriteValueByKey(sessionIdKey, uuid);
                    return uuid;
                });
    }
}
