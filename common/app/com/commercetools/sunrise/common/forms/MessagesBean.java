package com.commercetools.sunrise.common.forms;

import io.sphere.sdk.models.Base;

import java.util.List;

public class MessagesBean extends Base {

    private List<ErrorBean> globalErrors;

    public List<ErrorBean> getGlobalErrors() {
        return globalErrors;
    }

    public void setGlobalErrors(final List<ErrorBean> globalErrors) {
        this.globalErrors = globalErrors;
    }
}
