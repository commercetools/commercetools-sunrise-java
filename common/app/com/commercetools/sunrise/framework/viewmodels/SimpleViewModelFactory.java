package com.commercetools.sunrise.framework.viewmodels;

public abstract class SimpleViewModelFactory<M extends ViewModel, I> extends ViewModelFactory {

    protected abstract M newViewModelInstance(final I input);

    public M create(final I input) {
        return initializedViewModel(input);
    }

    protected abstract void initialize(final M viewModel, final I input);

    private M initializedViewModel(final I input) {
        final M viewModel = newViewModelInstance(input);
        initialize(viewModel, input);
        return viewModel;
    }

}
