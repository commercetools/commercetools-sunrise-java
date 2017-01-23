package com.commercetools.sunrise.common.models;

public abstract class CommonViewModelFactory<T> {

    protected abstract T getViewModelInstance();

    protected abstract void initialize(final T bean);

    protected final T initializedViewModel() {
        final T bean = getViewModelInstance();
        initialize(bean);
        return bean;
    }

}
