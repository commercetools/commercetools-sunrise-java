package com.commercetools.sunrise.framework.viewmodels.content;

import com.commercetools.sunrise.framework.viewmodels.FormViewModelFactory;
import play.data.Form;

public abstract class FormPageContentFactory<M extends PageContent, I, F> extends FormViewModelFactory<M, I, F> {

    @Override
    protected void initialize(final M viewModel, final I input, final Form<? extends F> form) {
        fillTitle(viewModel, input, form);
    }

    protected abstract void fillTitle(final M viewModel, final I input, final Form<? extends F> form);
}
