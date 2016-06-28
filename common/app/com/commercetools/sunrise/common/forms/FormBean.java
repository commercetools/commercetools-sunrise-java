package com.commercetools.sunrise.common.forms;

import io.sphere.sdk.models.Base;

public class FormBean extends Base {

    private ErrorsBean errors;
    private MessagesBean messages;

    public ErrorsBean getErrors() {
        return errors;
    }

    public void setErrors(final ErrorsBean errors) {
        this.errors = errors;
    }

    public MessagesBean getMessages() {
        return messages;
    }

    public void setMessages(final MessagesBean messages) {
        this.messages = messages;
    }
}
