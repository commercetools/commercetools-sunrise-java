package com.commercetools.sunrise.ctp;

import io.sphere.sdk.client.ErrorResponseException;
import io.sphere.sdk.customers.errors.CustomerInvalidCredentials;
import io.sphere.sdk.models.errors.DuplicateFieldError;

import javax.annotation.Nullable;

public final class CtpExceptionUtils {

    private CtpExceptionUtils() {
    }

    public static boolean isInvalidInputError(@Nullable final Throwable throwable) {
        return throwable instanceof ErrorResponseException
                && ((ErrorResponseException) throwable).hasErrorCode("InvalidInput");
    }

    public static boolean isInvalidOperationError(@Nullable final Throwable throwable) {
        return throwable instanceof ErrorResponseException
                && ((ErrorResponseException) throwable).hasErrorCode("InvalidOperation");
    }

    public static boolean isCustomerInvalidCredentialsError(@Nullable final Throwable throwable) {
        return throwable instanceof ErrorResponseException
                && ((ErrorResponseException) throwable).hasErrorCode(CustomerInvalidCredentials.CODE);
    }

    public static boolean isDuplicatedEmailFieldError(@Nullable final Throwable throwable) {
        return throwable instanceof ErrorResponseException
                && ((ErrorResponseException) throwable).getErrors().stream()
                .filter(error -> error.getCode().equals(DuplicateFieldError.CODE))
                .map(error -> error.as(DuplicateFieldError.class).getField())
                .anyMatch(duplicatedField -> duplicatedField != null && duplicatedField.equals("email"));
    }
}
