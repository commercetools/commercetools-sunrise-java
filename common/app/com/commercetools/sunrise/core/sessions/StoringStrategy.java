package com.commercetools.sunrise.core.sessions;

import com.google.inject.ImplementedBy;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Represents the strategy to be used to read and write information from the user.
 */
@ImplementedBy(CachedStoringStrategy.class)
public interface StoringStrategy {

    /**
     * Finds the object of type {@code <U>} associated with the given key in session.
     * @param key the session key
     * @param clazz class of the expected returned object
     * @param <U> type of the object to be returned
     * @return the object if found, empty if it could not be found or the class was not the expected one
     */
    <U> Optional<U> findInSession(String key, Class<U> clazz);

    /**
     * Finds the value associated with the given key in session.
     * @param key the session key
     * @return the value if found, empty otherwise
     */
    Optional<String> findInSession(String key);

    /**
     * Saves the object associated with the given key in session, replacing it if it already existed.
     * If {@code value} is null, then the object is removed.
     * @param key the session key
     * @param object the object to be stored
     * @param <U> type of the object to be stored
     */
    <U> void overwriteInSession(String key, @Nullable U object);

    /** Saves the value associated with the given key in session, replacing it if it already existed.
     * If {@code value} is null, then the value is removed from session.
     * @param key the session key
     * @param value the value to be stored
     */
    void overwriteInSession(String key, @Nullable String value);

    /**
     * Removes the object associated with the given key from session.
     * @param key the session key
     */
    void removeFromSession(String key);

    /**
     * Finds the object of type {@code <U>} associated with the given name in the cookies.
     * @param name the cookie name
     * @param clazz class of the expected returned object
     * @param <U> type of the object to be returned
     * @return the object if found, empty if it could not be found or the class was not the expected one
     */
    <U> Optional<U> findInCookies(String name, Class<U> clazz);

    /**
     * Finds the value associated with the given name in the cookies.
     * @param name the cookie name
     * @return the value if found, empty otherwise
     */
    Optional<String> findInCookies(String name);

    /**
     * Saves the object associated with the given name in the cookies, replacing it if it already existed.
     * If {@code value} is null, then the cookie is removed.
     * @param name the cookie name
     * @param object the object to be stored
     * @param <U> type of the object to be stored
     */
    <U> void overwriteInCookies(String name, @Nullable U object, boolean httpOnly, boolean secure);

    /**
     * Saves the value associated with the given name in the cookies, replacing it if it already existed.
     * If {@code value} is null, then the cookie is removed.
     * @param key the cookie name
     * @param value the value to be stored
     */
    void overwriteInCookies(String key, @Nullable String value, boolean httpOnly, boolean secure);

    /**
     * Removes the object associated with the given name from the cookies.
     * @param name the cookie name
     */
    void removeFromCookies(String name);
}
