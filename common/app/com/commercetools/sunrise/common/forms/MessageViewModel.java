package com.commercetools.sunrise.common.forms;

import com.commercetools.sunrise.common.models.ViewModel;

public class MessageViewModel extends ViewModel {

    private String message;

    public MessageViewModel() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }
}
