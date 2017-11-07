package com.commercetools.sunrise.ctp;

import com.commercetools.sunrise.ctp.models.errors.CustomerInvalidCurrentPassword;
import io.sphere.sdk.client.ErrorResponseException;
import io.sphere.sdk.customers.errors.CustomerInvalidCredentials;
import io.sphere.sdk.models.errors.DuplicateFieldError;
import io.sphere.sdk.orders.errors.DiscountCodeNonApplicableError;

import javax.annotation.Nullable;

public final class CtpExceptionUtils {

    private CtpExceptionUtils() {
    }

    public static boolean isInvalidInputError(@Nullable final Throwable throwable) {
        return isErrorResponseWithCode(throwable, "InvalidInput");
    }

    public static boolean isInvalidOperationError(@Nullable final Throwable throwable) {
        return isErrorResponseWithCode(throwable, "InvalidOperation");
    }

    public static boolean isCustomerInvalidCredentialsError(@Nullable final Throwable throwable) {
        return isErrorResponseWithCode(throwable, CustomerInvalidCredentials.CODE);
    }

    public static boolean isCustomerInvalidCurrentPasswordError(@Nullable final Throwable throwable) {
        return isErrorResponseWithCode(throwable, CustomerInvalidCurrentPassword.CODE);
    }

    public static boolean isDiscountCodeNonApplicableError(@Nullable final Throwable throwable) {
        return isErrorResponseWithCode(throwable, DiscountCodeNonApplicableError.CODE);
    }

    public static boolean isDuplicatedEmailFieldError(@Nullable final Throwable throwable) {
        return throwable instanceof ErrorResponseException
                && ((ErrorResponseException) throwable).getErrors().stream()
                .filter(error -> error.getCode().equals(DuplicateFieldError.CODE))
                .map(error -> error.as(DuplicateFieldError.class).getField())
                .anyMatch(duplicatedField -> duplicatedField != null && duplicatedField.equals("email"));
    }

    private static boolean isErrorResponseWithCode(final @Nullable Throwable throwable, final String code) {
        return throwable instanceof ErrorResponseException
                && ((ErrorResponseException) throwable).hasErrorCode(code);
    }
}
