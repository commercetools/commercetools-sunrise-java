package com.commercetools.sunrise.sessions;

import com.google.inject.ImplementedBy;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Represents a strategy used to read and write information into the user's session.
 */
@ImplementedBy(SessionCookieStrategy.class)
public interface SessionStrategy {

    /**
     * Finds the value associated with the given key in session.
     * @param key the session key
     * @return the value found in session, or empty if not found
     */
    Optional<String> findValueByKey(final String key);

    /**
     * Saves the value associated with the given key in session, replacing it if it already existed.
     * If {@code value} is null, then the key is removed from session.
     * @param key the session key
     * @param value the value to be set in session
     */
    void overwriteValueByKey(final String key, @Nullable final String value);

    /**
     * Removes the value associated with the given key from session.
     * @param key the session key
     */
    void removeValueByKey(final String key);
}
