package com.commercetools.sunrise.ctp.models.errors;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.sphere.sdk.models.errors.SphereError;

public final class CustomerInvalidCurrentPassword extends SphereError {
    public static final String CODE = "InvalidCurrentPassword";

    @JsonCreator
    private CustomerInvalidCurrentPassword(final String message) {
        super(CODE, message);
    }

    public static CustomerInvalidCurrentPassword of(final String message) {
        return new CustomerInvalidCurrentPassword(message);
    }
}
