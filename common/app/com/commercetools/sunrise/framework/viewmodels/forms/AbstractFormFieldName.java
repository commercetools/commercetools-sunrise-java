package com.commercetools.sunrise.framework.viewmodels.forms;

import com.commercetools.sunrise.framework.SunriseModel;

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
