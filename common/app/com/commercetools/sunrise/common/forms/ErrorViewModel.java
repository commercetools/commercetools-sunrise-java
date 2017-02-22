package com.commercetools.sunrise.common.forms;

import com.commercetools.sunrise.common.models.ViewModel;

public class ErrorViewModel extends ViewModel {

    private String message;

    public ErrorViewModel() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }
}
