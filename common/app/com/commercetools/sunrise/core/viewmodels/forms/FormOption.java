package com.commercetools.sunrise.core.viewmodels.forms;

public interface FormOption<T> {

    String getFieldLabel();

    String getFieldValue();

    T getValue();

    boolean isDefault();
}
