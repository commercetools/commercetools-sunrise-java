package com.commercetools.sunrise.framework.viewmodels.forms;

import com.commercetools.sunrise.framework.SunriseModel;

public abstract class AbstractFormOption<T> extends SunriseModel implements FormOption<T> {

    private final String fieldLabel;
    private final String fieldValue;
    private final T value;
    private final boolean isDefault;

    protected AbstractFormOption(final String fieldLabel, final String fieldValue, final T value, final boolean isDefault) {
        this.fieldLabel = fieldLabel;
        this.fieldValue = fieldValue;
        this.value = value;
        this.isDefault = isDefault;
    }

    @Override
    public String getFieldLabel() {
        return fieldLabel;
    }

    @Override
    public String getFieldValue() {
        return fieldValue;
    }

    @Override
    public T getValue() { return value; }

    @Override
    public boolean isDefault() {
        return isDefault;
    }
}
