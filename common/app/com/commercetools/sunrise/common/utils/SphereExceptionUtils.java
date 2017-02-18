package com.commercetools.sunrise.common.utils;

import io.sphere.sdk.client.ClientErrorException;
import io.sphere.sdk.client.ErrorResponseException;
import io.sphere.sdk.customers.errors.CustomerInvalidCredentials;
import io.sphere.sdk.models.errors.DuplicateFieldError;

public final class SphereExceptionUtils {

    private SphereExceptionUtils() {
    }

    public static boolean isCustomerInvalidCredentialsError(final ClientErrorException clientErrorException) {
        return clientErrorException instanceof ErrorResponseException
                && ((ErrorResponseException) clientErrorException).hasErrorCode(CustomerInvalidCredentials.CODE);
    }

    public static boolean isDuplicatedEmailFieldError(final ClientErrorException clientErrorException) {
        return clientErrorException instanceof ErrorResponseException
                && ((ErrorResponseException) clientErrorException).getErrors().stream()
                .filter(error -> error.getCode().equals(DuplicateFieldError.CODE))
                .map(error -> error.as(DuplicateFieldError.class).getField())
                .anyMatch(duplicatedField -> duplicatedField != null && duplicatedField.equals("email"));
    }
}
