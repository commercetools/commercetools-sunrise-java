package com.commercetools.sunrise.common.forms;

import io.sphere.sdk.models.Base;
import play.data.Form;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.singletonList;

public class ErrorsBean extends Base {

    private List<ErrorBean> globalErrors;

    public ErrorsBean() {
    }

    public ErrorsBean(final String errorMessage) {
        this.globalErrors = singletonList(new ErrorBean(errorMessage));
    }

    @Deprecated
    public ErrorsBean(final List<Form<?>> filledForms) {
        this.globalErrors = new ArrayList<>();
        filledForms.forEach(filledForm ->
                filledForm.errors().forEach((field, errors) ->
                        errors.forEach(error ->
                                globalErrors.add(new ErrorBean(error.key() + ": " + error.message())))));
    }

    @Deprecated
    public ErrorsBean(final Form<?> filledForm) {
        this(singletonList(filledForm));
    }

    public List<ErrorBean> getGlobalErrors() {
        return globalErrors;
    }

    public void setGlobalErrors(final List<ErrorBean> globalErrors) {
        this.globalErrors = globalErrors;
    }
}
