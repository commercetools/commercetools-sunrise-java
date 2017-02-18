package com.commercetools.sunrise.sessions;

import javax.annotation.Nullable;

/**
 * Enables a particular type to be stored somewhere.
 * The interface does not define where (e.g. session, cache, memory) or how (e.g. the entire object, only specific parts)
 * the provided object is stored, which should be specified by the implementation class.
 * @param <T> Class of the stored object
 */
public interface ResourceStoringOperations<T> {

    /**
     * Stores the object, replacing it if a previous version already existed.
     * If the value is {@code null}, it removes the object instead.
     * @param value the instance of the object used to update the stored data, or {@code null} to remove all data
     */
    void store(@Nullable final T value);

    /**
     * Removes the stored object, if any.
     */
    void remove();
}
