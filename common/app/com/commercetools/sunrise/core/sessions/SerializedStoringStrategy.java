package com.commercetools.sunrise.core.sessions;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Uses a session cookie to store string information about the user.
 * Objects are first serialized to be able to write them into the session cookie.
 * Notice that cookies have a very limited space, so do not use this strategy to store a large amount of data.
 */
public abstract class SerializedStoringStrategy extends AbstractStoringStrategy {

    @Override
    public <U> Optional<U> findInSession(final String key, final Class<U> clazz) {
        return findInSession(key).flatMap(serializedObj -> deserializeObject(clazz, serializedObj));
    }

    @Override
    public <U> void overwriteInSession(final String key, @Nullable final U obj) {
        overwriteInSession(key, serializeObject(obj).orElse(null));
    }

    @Override
    public <U> Optional<U> findInCookies(final String name, final Class<U> clazz) {
        return findInCookies(name).flatMap(serializedObject -> deserializeObject(clazz, serializedObject));
    }

    @Override
    public <U> void overwriteInCookies(final String name, @Nullable final U obj, final boolean httpOnly, final boolean secure) {
        overwriteInCookies(name, serializeObject(obj).orElse(null), httpOnly, secure);
    }

    protected abstract <U> Optional<U> deserializeObject(final Class<U> clazz, final String serializedObj);

    protected abstract <U> Optional<String> serializeObject(@Nullable final U obj);
}