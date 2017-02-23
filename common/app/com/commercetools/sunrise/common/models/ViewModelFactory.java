package com.commercetools.sunrise.common.models;

public abstract class ViewModelFactory<M extends ViewModel, I> {

    protected abstract M getViewModelInstance(final I input);

    public M create(final I input) {
        return initializedViewModel(input);
    }

    protected abstract void initialize(final M viewModel, final I input);

    protected final M initializedViewModel(final I input) {
        final M viewModel = getViewModelInstance(input);
        initialize(viewModel, input);
        return viewModel;
    }

}
