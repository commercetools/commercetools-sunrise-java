package com.commercetools.sunrise.sessions;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;

import javax.annotation.Nullable;
import javax.inject.Singleton;
import java.util.Optional;

/**
 * Uses a session cookie to store string information about the user.
 * Objects are first serialized to JSON to be able to write them into the session cookie.
 * Notice that cookies have a very limited space, so do not use this strategy to store a large amount of data.
 */
@Singleton
public final class SerializableObjectStoringSessionCookieStrategy extends SessionCookieStrategy implements ObjectStoringSessionStrategy {

    /**
     * {@inheritDoc}
     */
    @Override
    public <U> Optional<U> findObjectByKey(final String key, final Class<U> clazz) {
        return findValueByKey(key)
                .flatMap(valueAsJson -> {
                    try {
                        final U value = Json.fromJson(Json.parse(valueAsJson), clazz);
                        return Optional.of(value);
                    } catch (RuntimeException e) {
                        logger.error("Could not parse value in session key \"{}\" into type \"{}\"", key, clazz.getSimpleName(), e);
                        return Optional.empty();
                    }
                });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <U> void overwriteObjectByKey(final String key, @Nullable final U object) {
        if (object != null) {
            final JsonNode jsonNode = Json.toJson(object);
            final String valueAsJson = Json.stringify(jsonNode);
            overwriteValueByKey(key, valueAsJson);
        } else {
            removeValueByKey(key);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeObjectByKey(final String key) {
        removeValueByKey(key);
    }
}