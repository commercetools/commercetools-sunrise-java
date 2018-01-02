package com.commercetools.sunrise.core.sessions;

import com.fasterxml.jackson.databind.JsonNode;
import io.sphere.sdk.json.SphereJsonUtils;
import io.sphere.sdk.models.Resource;
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
public final class JsonStoringStrategy extends SerializedStoringStrategy {

    @Override
    protected <U> Optional<U> deserializeObject(final Class<U> clazz, final String serializedObj) {
        try {
            final JsonNode jsonNode = Json.parse(serializedObj);
            return Optional.of(Json.fromJson(jsonNode, clazz));
        } catch (RuntimeException e) {
            LOGGER.error("Could not deserialize stored object \"{}\" into type \"{}\"", serializedObj, clazz.getSimpleName(), e);
            return Optional.empty();
        }
    }

    @Override
    protected <U> Optional<String> serializeObject(@Nullable final U obj) {
        return Optional.ofNullable(obj).map(object -> Json.stringify(convertToJson(obj)));
    }

    private <U> JsonNode convertToJson(@Nullable final U obj) {
        return obj instanceof Resource ? SphereJsonUtils.toJsonNode(obj) : Json.toJson(obj);
    }
}