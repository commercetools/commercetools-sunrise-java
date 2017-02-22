package com.commercetools.sunrise.common.models;

import com.commercetools.sunrise.common.forms.FormFieldWithOptions;
import play.data.Form;

import java.util.List;

public abstract class FormFieldViewModelFactory<T extends ViewModel, D> extends ViewModelFactory<T, FormFieldWithOptions<D>> {

    public T createWithDefaultOptions(final Form.Field formField) {
        return create(new FormFieldWithOptions<>(formField, defaultOptions()));
    }

    protected abstract List<D> defaultOptions();
}
