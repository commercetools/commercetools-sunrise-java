package com.commercetools.sunrise.common.models;

import com.commercetools.sunrise.common.forms.FormField;
import com.commercetools.sunrise.common.forms.FormFieldWithOptions;

import java.util.List;

public abstract class FormFieldViewModelFactory<T, D> extends ViewModelFactory<T, FormFieldWithOptions<D>> {

    public T createWithDefaultOptions(final FormField data) {
        return create(new FormFieldWithOptions<>(data.form, data.formFieldName, defaultOptions()));
    }

    protected abstract List<D> defaultOptions();
}
