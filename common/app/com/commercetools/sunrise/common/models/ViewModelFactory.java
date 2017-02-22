package com.commercetools.sunrise.common.models;

public abstract class ViewModelFactory<M extends ViewModel, I> {

    protected abstract M getViewModelInstance();

    public M create(final I input) {
        return initializedViewModel(input);
    }

    protected abstract void initialize(final M viewModel, final I input);

    protected final M initializedViewModel(final I input) {
        final M model = getViewModelInstance();
        initialize(model, input);
        return model;
    }

}
