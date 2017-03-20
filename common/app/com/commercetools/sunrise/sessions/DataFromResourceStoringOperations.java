package com.commercetools.sunrise.sessions;

import org.slf4j.Logger;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

/**
 * Allows to store only some selected parts of information derived from the object, in opposite to the whole object.
 * This might come in handy when the object contains sensible or unused data, it is too large or different data
 * requires different storing strategies (e.g. save the cart ID in session and the mini cart in the session cache).
 * @param <T> Class of the stored object
 */
public abstract class DataFromResourceStoringOperations<T> implements ResourceStoringOperations<T> {

    protected abstract Logger getLogger();

    /**
     * {@inheritDoc}
     */
    @Override
    public void store(@Nullable final T value) {
        if (value != null) {
            storeAssociatedData(value);
        } else {
            removeAssociatedData();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove() {
        removeAssociatedData();
    }

    /**
     * Stores the data related to the object, replacing it if a previous version already existed.
     * @param value the instance of the object used to update the stored data
     */
    protected abstract void storeAssociatedData(@NotNull final T value);

    /**
     * Removes the stored data related to the object.
     */
    protected abstract void removeAssociatedData();
}