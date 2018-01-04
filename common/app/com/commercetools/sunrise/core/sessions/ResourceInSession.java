package com.commercetools.sunrise.core.sessions;

import io.sphere.sdk.models.Resource;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Specialization of {@link StoringOperations<T>} with commercetools {@link Resource<T>}.
 * @param <T> Class of the stored resource
 */
public interface ResourceInSession<T extends Resource<T>> extends StoringOperations<T> {

    Optional<String> findId();

    Optional<Long> findVersion();

    /**
     * Stores the resource (or some parts), replacing it if a previous version already existed.
     * If the resource is {@code null}, it removes the object instead.
     * @param resource the instance of the object used to update the stored data, or {@code null} to remove all data
     */
    void store(@Nullable final T resource);

    /**
     * Removes the stored object, if any.
     */
    void remove();
}
