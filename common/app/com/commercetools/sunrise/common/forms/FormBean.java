package com.commercetools.sunrise.common.forms;

import com.commercetools.sunrise.common.models.ModelBean;

public class FormBean extends ModelBean {

    private ErrorsBean errors;
    private MessagesBean messages;

    public FormBean() {
    }

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
