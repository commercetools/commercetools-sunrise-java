package com.commercetools.sunrise.common.models;

public abstract class ViewModelFactory<T extends ViewModel, D> {

    protected abstract T getViewModelInstance();

    public T create(final D data) {
        return initializedViewModel(data);
    }

    protected abstract void initialize(final T model, final D data);

    protected final T initializedViewModel(final D data) {
        final T model = getViewModelInstance();
        initialize(model, data);
        return model;
    }

}
