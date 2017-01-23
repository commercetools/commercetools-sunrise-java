package com.commercetools.sunrise.common.models;

public abstract class FormFieldFactory<T, D> {

    protected abstract T getViewModelInstance();

    protected abstract void initialize(final T bean, final D data);

    protected final T initializedViewModel(final D data) {
        final T bean = getViewModelInstance();
        initialize(bean, data);
        return bean;
    }
}
