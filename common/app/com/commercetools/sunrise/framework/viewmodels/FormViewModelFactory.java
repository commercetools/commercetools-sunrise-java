package com.commercetools.sunrise.framework.viewmodels;

import play.data.Form;

public abstract class FormViewModelFactory<M extends ViewModel, I, F> extends ViewModelFactory {

    protected abstract M newViewModelInstance(final I input, final Form<? extends F> form);

    public M create(final I input, final Form<? extends F> form) {
        return initializedViewModel(input, form);
    }

    protected abstract void initialize(final M viewModel, final I input, final Form<? extends F> form);

    private M initializedViewModel(final I input, final Form<? extends F> form) {
        final M viewModel = newViewModelInstance(input, form);
        initialize(viewModel, input, form);
        return viewModel;
    }
}
