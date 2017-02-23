package com.commercetools.sunrise.common.pages;

import com.commercetools.sunrise.common.models.ViewModelFactory;

public class PageFooterFactory extends ViewModelFactory<PageFooter, PageContent> {

    @Override
    protected final PageFooter newViewModelInstance(final PageContent pageContent) {
        return new PageFooter();
    }

    @Override
    protected final void initialize(final PageFooter viewModel, final PageContent content) {
    }
}
