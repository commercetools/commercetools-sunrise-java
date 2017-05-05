package com.commercetools.sunrise.framework.viewmodels.forms;

public abstract class AbstractFormSettings<T> extends AbstractFormFieldName implements FormSettings<T> {

    protected AbstractFormSettings(final String fieldName) {
        super(fieldName);
    }
}
