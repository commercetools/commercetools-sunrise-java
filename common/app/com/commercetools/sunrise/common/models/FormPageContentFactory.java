package com.commercetools.sunrise.common.models;

import com.commercetools.sunrise.common.pages.PageContent;
import play.data.Form;

public abstract class FormPageContentFactory<T extends PageContent, D, F> {

    protected abstract T getViewModelInstance();

    public T create(final D data, final Form<? extends F> form) {
        return initializedViewModel(data, form);
    }

    protected void initialize(final T model, final D data, final Form<? extends F> form) {
        fillTitle(model, data, form);
    }

    protected final T initializedViewModel(final D data, final Form<? extends F> form) {
        final T bean = getViewModelInstance();
        initialize(bean, data, form);
        return bean;
    }

    protected abstract void fillTitle(final T model, final D data, final Form<? extends F> form);
}
