package com.commercetools.sunrise.common.forms;

import io.sphere.sdk.models.Base;
import play.data.Form;

import javax.annotation.Nullable;

public class FormField extends Base {

    public final Form<?> form;
    public final String formFieldName;
    @Nullable
    public final String selectedValue;

    public FormField(final Form<?> form, final String formFieldName) {
        this.form = form;
        this.formFieldName = formFieldName;
        this.selectedValue = FormUtils.extractFormField(form, formFieldName);
    }
}
