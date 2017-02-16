package com.commercetools.sunrise.common.forms;

import com.commercetools.sunrise.common.models.SunriseModel;

public abstract class FormOption<D> extends SunriseModel {

    private final String fieldLabel;
    private final String fieldValue;
    private final D value;
    private final boolean isDefault;

    protected FormOption(final String fieldLabel, final String fieldValue, final D value, final boolean isDefault) {
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
