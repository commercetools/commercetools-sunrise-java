package com.commercetools.sunrise.common.models;

import com.commercetools.sunrise.common.forms.FormFieldWithOptions;
import play.data.Form;

import java.util.List;

public abstract class FormFieldViewModelFactory<M extends ViewModel, I> extends ViewModelFactory<M, FormFieldWithOptions<I>> {

    public M createWithDefaultOptions(final Form.Field formField) {
        return create(new FormFieldWithOptions<>(formField, defaultOptions()));
    }

    protected abstract List<I> defaultOptions();
}
