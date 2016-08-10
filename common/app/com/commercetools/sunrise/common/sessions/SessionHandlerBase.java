package com.commercetools.sunrise.common.sessions;

import io.sphere.sdk.json.JsonException;
import io.sphere.sdk.json.SphereJsonUtils;
import org.slf4j.Logger;
import play.libs.Json;
import play.mvc.Http;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Set;

public abstract class SessionHandlerBase<T> implements SessionHandler<T> {

    @Override
    public void overwriteInSession(final Http.Session session, @Nullable final T value) {
        if (value != null) {
            overwriteRelatedValuesInSession(session, value);
        } else {
            removeRelatedValuesFromSession(session);
        }
    }

    @Override
    public void removeFromSession(final Http.Session session) {
        removeRelatedValuesFromSession(session);
    }

    protected abstract void overwriteRelatedValuesInSession(final Http.Session session, final T value);

    protected void removeRelatedValuesFromSession(final Http.Session session) {
        sessionKeys().forEach(key -> removeValue(session, key));
    }

    protected Optional<String> findValue(final Http.Session session, final String key) {
        return Optional.ofNullable(session.get(key));
    }

    protected <U> Optional<U> findValue(final Http.Session session, final String key, final Class<U> clazz) {
        return findValue(session, key)
                .flatMap(valueAsJson -> {
                    try {
                        return Optional.of(SphereJsonUtils.readObject(valueAsJson, clazz));
                    } catch (JsonException e) {
                        logger().error("Could not parse value in session key \"{}\"", key, e);
                        return Optional.empty();
                    }
                });
    }

    protected void overwriteValue(final Http.Session session, final String key, final String value) {
        session.put(key, value);
        logger().debug("Saved in session \"{}\" = {}", key, value);
    }

    protected <U> void overwriteValue(final Http.Session session, final String key, final U value) {
        final String valueAsJson = Json.stringify(SphereJsonUtils.toJsonNode(value));
        overwriteValue(session, key, valueAsJson);
    }

    protected void removeValue(final Http.Session session, final String key) {
        session.remove(key);
        logger().debug("Removed from session \"{}\"", key);
    }

    protected abstract Set<String> sessionKeys();

    protected abstract Logger logger();
}
