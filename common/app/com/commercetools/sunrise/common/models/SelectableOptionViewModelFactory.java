package com.commercetools.sunrise.common.models;

import javax.annotation.Nullable;

public abstract class SelectableOptionViewModelFactory<T, D> {

    protected abstract T getViewModelInstance();

    public T create(final D option, @Nullable final String selectedValue) {
        return initializedViewModel(option, selectedValue);
    }

    protected abstract void initialize(final T model, final D option, @Nullable final String selectedValue);

    protected final T initializedViewModel(final D option, @Nullable final String selectedValue) {
        final T bean = getViewModelInstance();
        initialize(bean, option, selectedValue);
        return bean;
    }
}
