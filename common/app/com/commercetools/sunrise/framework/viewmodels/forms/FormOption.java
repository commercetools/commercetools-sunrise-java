package com.commercetools.sunrise.framework.viewmodels.forms;

public interface FormOption<T> {

    String getFieldLabel();

    String getFieldValue();

    T getValue();

    boolean isDefault();
}
