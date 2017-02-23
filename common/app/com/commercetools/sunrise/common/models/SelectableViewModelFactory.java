package com.commercetools.sunrise.common.models;

import javax.annotation.Nullable;

public abstract class SelectableViewModelFactory<M, O, S> {

    protected abstract M newViewModelInstance(final O option, @Nullable final S selectedValue);

    public M create(final O option, @Nullable final S selectedValue) {
        return initializedViewModel(option, selectedValue);
    }

    protected abstract void initialize(final M viewModel, final O option, @Nullable final S selectedValue);

    protected final M initializedViewModel(final O option, @Nullable final S selectedValue) {
        final M viewModel = newViewModelInstance(option, selectedValue);
        initialize(viewModel, option, selectedValue);
        return viewModel;
    }
}
