package com.commercetools.sunrise.framework.viewmodels.forms;

import com.commercetools.sunrise.framework.SunriseModel;
import play.data.Form;

import java.util.List;

public final class FormFieldWithOptions<T> extends SunriseModel {

    private final Form.Field formField;
    private final List<T> formOptions;

    private FormFieldWithOptions(final Form.Field formField, final List<T> formOptions) {
        this.formField = formField;
        this.formOptions = formOptions;
    }

    public final Form.Field getFormField() {
        return formField;
    }

    public final List<T> getFormOptions() {
        return formOptions;
    }

    public static <T> FormFieldWithOptions<T> of(final Form.Field formField, final List<T> formOptions) {
        return new FormFieldWithOptions<>(formField, formOptions);
    }
}
