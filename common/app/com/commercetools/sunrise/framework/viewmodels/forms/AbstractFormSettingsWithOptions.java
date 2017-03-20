package com.commercetools.sunrise.framework.viewmodels.forms;

import java.util.List;

public abstract class AbstractFormSettingsWithOptions<T extends FormOption<V>, V> extends AbstractFormFieldName implements FormSettingsWithOptions<T, V> {

    private final List<T> options;

    protected AbstractFormSettingsWithOptions(final String fieldName, final List<T> options) {
        super(fieldName);
        this.options = options;
    }

    @Override
    public List<T> getOptions() {
        return options;
    }
}
