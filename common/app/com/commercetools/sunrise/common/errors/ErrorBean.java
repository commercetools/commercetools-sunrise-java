package com.commercetools.sunrise.common.errors;

import io.sphere.sdk.models.Base;

public class ErrorBean extends Base {
    private String message;

    public ErrorBean() {
    }

    public ErrorBean(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }
}
