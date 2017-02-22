package com.commercetools.sunrise.common.models;

import play.data.Form;

public abstract class FormViewModelFactory<M extends ViewModel, I, F>  {

    protected abstract M getViewModelInstance();

    public M create(final I input, final Form<? extends F> form) {
        return initializedViewModel(input, form);
    }

    protected abstract void initialize(final M viewModel, final I input, final Form<? extends F> form);

    protected final M initializedViewModel(final I data, final Form<? extends F> form) {
        final M viewModel = getViewModelInstance();
        initialize(viewModel, data, form);
        return viewModel;
    }
}
