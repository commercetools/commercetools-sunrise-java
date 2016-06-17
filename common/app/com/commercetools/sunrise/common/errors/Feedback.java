package com.commercetools.sunrise.common.errors;

import io.sphere.sdk.models.Base;

import java.util.List;

public class Feedback extends Base {

    private List<String> errors;
    private List<String> messages;

    public Feedback() {
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(final List<String> errors) {
        this.errors = errors;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(final List<String> messages) {
        this.messages = messages;
    }
}
