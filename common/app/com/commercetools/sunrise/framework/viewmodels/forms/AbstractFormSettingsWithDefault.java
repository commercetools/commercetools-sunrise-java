package com.commercetools.sunrise.framework.viewmodels.forms;

public abstract class AbstractFormSettingsWithDefault<T> extends AbstractFormFieldName implements FormSettingsWithDefault<T> {

    private final T defaultValue;

    protected AbstractFormSettingsWithDefault(final String fieldName, final T defaultValue) {
        super(fieldName);
        this.defaultValue = defaultValue;
    }

    @Override
    public T getDefaultValue() {
        return defaultValue;
    }
}
