package com.commercetools.sunrise.common.sessions;

import com.fasterxml.jackson.databind.JsonNode;
import io.sphere.sdk.json.JsonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.Json;
import play.mvc.Http;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * Class to use as a base to all session managers, with all common logic.
 * @param <T> Class of the object in session
 */
public abstract class SessionHandlerBase<T> implements SessionHandler<T> {

    /**
     * Takes care of overwriting all related values to this object.
     * For example, in the case of the cart, those might be overwriting the cart ID value, the mini cart instance, etc.
     * @param session the HTTP session to be updated
     * @param value the instance of the object used to update the session
     */
    protected abstract void overwriteRelatedValuesInSession(final Http.Session session, @NotNull final T value);

    /**
     * Takes care of overwriting all related values to this object.
     * For example, in the case of the cart, those might be removing the cart ID value, the mini cart instance, etc.
     * @param session the HTTP session to be updated
     */
    protected abstract void removeRelatedValuesFromSession(final Http.Session session);

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

    protected Optional<String> findValueByKey(final Http.Session session, final String key) {
        return Optional.ofNullable(session.get(key));
    }

    protected <U> Optional<U> findValueByKey(final Http.Session session, final String key, final Class<U> clazz) {
        return findValueByKey(session, key)
                .flatMap(valueAsJson -> {
                    try {
                        final JsonNode jsonNode = Json.parse(valueAsJson);
                        return Optional.of(Json.fromJson(jsonNode, clazz));
                    } catch (JsonException e) {
                        logger().error("Could not parse value in session key \"{}\"", key, e);
                        return Optional.empty();
                    }
                });
    }

    protected void overwriteStringValueByKey(final Http.Session session, final String key, final String value) {
        session.put(key, value);
        logger().debug("Saved in session \"{}\" = {}", key, value);
    }

    protected <U> void overwriteValueByKey(final Http.Session session, final String key, final U value) {
        final String valueAsJson = Json.stringify(Json.toJson(value));
        overwriteStringValueByKey(session, key, valueAsJson);
    }

    protected void removeValuesByKey(final Http.Session session, final List<String> keys) {
        keys.forEach(key -> removeValuesByKey(session, key));
    }

    protected void removeValuesByKey(final Http.Session session, final String key) {
        session.remove(key);
        logger().debug("Removed from session \"{}\"", key);
    }

    public Logger logger() {
        return LoggerFactory.getLogger(getClass());
    }
}
