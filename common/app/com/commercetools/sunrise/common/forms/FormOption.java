package com.commercetools.sunrise.common.forms;

import io.sphere.sdk.models.Base;

public abstract class FormOption<D> extends Base {

    private final String fieldLabel;
    private final String fieldValue;
    private final D value;
    private final boolean isDefault;

    public FormOption(final String fieldLabel, final String fieldValue, final D value, final boolean isDefault) {
        this.fieldLabel = fieldLabel;
        this.fieldValue = fieldValue;
        this.value = value;
        this.isDefault = isDefault;
    }

    public String getFieldLabel() {
        return fieldLabel;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public D getValue() { return value; }

    public boolean isDefault() {
        return isDefault;
    }
}
