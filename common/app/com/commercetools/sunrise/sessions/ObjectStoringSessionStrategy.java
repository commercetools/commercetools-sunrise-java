package com.commercetools.sunrise.sessions;

import com.google.inject.ImplementedBy;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Represents a strategy used to read and write objects into the user's session.
 */
@ImplementedBy(SerializableObjectStoringSessionCookieStrategy.class)
public interface ObjectStoringSessionStrategy extends SessionStrategy {

    /**
     * Finds the object of type {@code <U>} associated with the given key in session.
     * @param key the session key
     * @param clazz class of the expected returned object
     * @param <U> type of the object to be returned
     * @return the object found in session, or empty if it could not be found or the class was not the expected one
     */
    <U> Optional<U> findObjectByKey(final String key, final Class<U> clazz);

    /**
     * Saves the object associated with the given key in session, replacing it if it already existed.
     * If {@code value} is null, then the key is removed from session.
     * @param key the session key
     * @param object the object to be set in session
     * @param <U> type of the object to be set
     */
    <U> void overwriteObjectByKey(final String key, @Nullable final U object);

    /**
     * Removes the object associated with the given key from session.
     * @param key the session key
     */
    void removeObjectByKey(final String key);
}
