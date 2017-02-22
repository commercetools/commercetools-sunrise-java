package com.commercetools.sunrise.common.models;

import com.commercetools.sunrise.common.pages.PageContent;

public abstract class PageContentFactory<M extends PageContent, I> extends ViewModelFactory<M, I> {

    @Override
    protected void initialize(final M viewModel, final I input) {
        fillTitle(viewModel, input);
    }

    protected abstract void fillTitle(final M viewModel, final I input);
}
