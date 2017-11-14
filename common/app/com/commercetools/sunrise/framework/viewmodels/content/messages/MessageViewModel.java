package com.commercetools.sunrise.framework.viewmodels.content.messages;

import com.commercetools.sunrise.framework.viewmodels.ViewModel;

public class MessageViewModel extends ViewModel {

    private String message;
    private String type;

    public MessageViewModel() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }
}
