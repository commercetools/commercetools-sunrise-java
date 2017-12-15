package com.commercetools.sunrise.core.viewmodels.forms;

public abstract class AbstractFormSettings<T> extends AbstractFormFieldName implements FormSettings<T> {

    protected AbstractFormSettings(final String fieldName) {
        super(fieldName);
    }
}
