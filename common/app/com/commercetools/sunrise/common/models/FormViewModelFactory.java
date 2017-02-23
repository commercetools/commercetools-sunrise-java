package com.commercetools.sunrise.common.models;

import play.data.Form;

public abstract class FormViewModelFactory<M extends ViewModel, I, F>  {

    protected abstract M newViewModelInstance(final I input);

    public M create(final I input, final Form<? extends F> form) {
        return initializedViewModel(input, form);
    }

    protected abstract void initialize(final M viewModel, final I input, final Form<? extends F> form);

    protected final M initializedViewModel(final I input, final Form<? extends F> form) {
        final M viewModel = newViewModelInstance(input);
        initialize(viewModel, input, form);
        return viewModel;
    }
}
