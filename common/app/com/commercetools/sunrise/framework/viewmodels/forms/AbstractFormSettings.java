package com.commercetools.sunrise.framework.viewmodels.forms;

public abstract class AbstractFormSettings<T> extends AbstractFormFieldName implements FormSettings<T> {

    private final T defaultValue;

    protected AbstractFormSettings(final String fieldName, final T defaultValue) {
        super(fieldName);
        this.defaultValue = defaultValue;
    }

    @Override
    public T getDefaultValue() {
        return defaultValue;
    }
}

