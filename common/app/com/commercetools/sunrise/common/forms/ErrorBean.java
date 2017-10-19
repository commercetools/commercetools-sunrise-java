package com.commercetools.sunrise.common.forms;

import io.sphere.sdk.models.Base;

public class ErrorBean extends Base {

    private String message;

    private String field;


    public ErrorBean() {
    }

    public ErrorBean(final String field, final String message) {
        this.field = field;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
}
