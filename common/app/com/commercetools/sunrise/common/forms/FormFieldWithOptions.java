package com.commercetools.sunrise.common.forms;

import io.sphere.sdk.models.Base;
import play.data.Form;

import java.util.List;

public class FormFieldWithOptions<T> extends Base {

    public final Form.Field formField;
    public final List<T> formOptions;

    public FormFieldWithOptions(final Form.Field formField, final List<T> formOptions) {
        this.formField = formField;
        this.formOptions = formOptions;
    }
}
