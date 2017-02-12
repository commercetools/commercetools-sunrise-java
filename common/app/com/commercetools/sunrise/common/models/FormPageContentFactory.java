package com.commercetools.sunrise.common.models;

import com.commercetools.sunrise.common.pages.PageContent;
import play.data.Form;

public abstract class FormPageContentFactory<T extends PageContent, D, F> extends FormViewModelFactory<T, D, F> {

    @Override
    protected void initialize(final T model, final D data, final Form<? extends F> form) {
        fillTitle(model, data, form);
    }

    protected abstract void fillTitle(final T model, final D data, final Form<? extends F> form);
}
