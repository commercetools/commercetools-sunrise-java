package com.commercetools.sunrise.common.forms;

import play.data.Form;

import java.util.List;

public class FormFieldWithOptions<T> extends FormField {

    public final List<T> formOptions;

    public FormFieldWithOptions(final Form<?> form, final String formFieldName, final List<T> formOptions) {
        super(form, formFieldName);
        this.formOptions = formOptions;
    }
}
