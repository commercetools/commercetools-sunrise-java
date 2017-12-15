package com.commercetools.sunrise.core.viewmodels.forms;

import com.commercetools.sunrise.core.SunriseModel;

public abstract class AbstractFormFieldName extends SunriseModel implements WithFormFieldName {

    private final String fieldName;

    protected AbstractFormFieldName(final String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }
}
