package com.commercetools.sunrise.framework.viewmodels.forms;

import com.commercetools.sunrise.framework.viewmodels.ViewModel;
import com.commercetools.sunrise.framework.viewmodels.SimpleViewModelFactory;
import play.data.Form;

import java.util.List;

public abstract class FormFieldViewModelFactory<M extends ViewModel, I> extends SimpleViewModelFactory<M, FormFieldWithOptions<I>> {

    public M createWithDefaultOptions(final Form.Field formField) {
        return create(FormFieldWithOptions.of(formField, defaultOptions()));
    }

    protected abstract List<I> defaultOptions();
}
