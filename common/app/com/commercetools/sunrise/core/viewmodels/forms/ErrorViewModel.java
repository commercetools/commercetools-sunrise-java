package com.commercetools.sunrise.core.viewmodels.forms;

import com.commercetools.sunrise.core.viewmodels.ViewModel;

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
