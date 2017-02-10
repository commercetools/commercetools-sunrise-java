package com.commercetools.sunrise.common.forms;

import io.sphere.sdk.models.Base;

public abstract class FormSettings<T> extends Base implements WithFormFieldName<T> {

    private final String fieldName;
    private final T defaultValue;

    protected FormSettings(final String fieldName, final T defaultValue) {
        this.fieldName = fieldName;
        this.defaultValue = defaultValue;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public abstract T mapToValue(final String valueAsString);

    public abstract boolean isValidValue(final T value);
}

