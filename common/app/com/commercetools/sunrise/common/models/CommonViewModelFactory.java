package com.commercetools.sunrise.common.models;

public abstract class CommonViewModelFactory<T> {

    protected abstract T getViewModelInstance();

    public T create() {
        return initializedViewModel();
    }

    protected abstract void initialize(final T model);

    protected final T initializedViewModel() {
        final T model = getViewModelInstance();
        initialize(model);
        return model;
    }

}
