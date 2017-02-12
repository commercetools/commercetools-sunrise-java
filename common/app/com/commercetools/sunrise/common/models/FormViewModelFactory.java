package com.commercetools.sunrise.common.models;

import play.data.Form;

public abstract class FormViewModelFactory<T, D, F>  {

    protected abstract T getViewModelInstance();

    public T create(final D data, final Form<? extends F> form) {
        return initializedViewModel(data, form);
    }

    protected abstract void initialize(final T model, final D data, final Form<? extends F> form);

    protected final T initializedViewModel(final D data, final Form<? extends F> form) {
        final T bean = getViewModelInstance();
        initialize(bean, data, form);
        return bean;
    }
}
