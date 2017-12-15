package com.commercetools.sunrise.core.viewmodels.footer;

import com.commercetools.sunrise.core.viewmodels.SimpleViewModelFactory;
import com.commercetools.sunrise.core.viewmodels.content.PageContent;

public class PageFooterFactory extends SimpleViewModelFactory<PageFooter, PageContent> {

    @Override
    protected final PageFooter newViewModelInstance(final PageContent pageContent) {
        return new PageFooter();
    }

    @Override
    protected final void initialize(final PageFooter viewModel, final PageContent content) {
    }
}
