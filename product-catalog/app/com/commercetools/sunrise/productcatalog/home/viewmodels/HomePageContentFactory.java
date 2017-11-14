package com.commercetools.sunrise.productcatalog.home.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.PageTitleResolver;
import com.commercetools.sunrise.framework.viewmodels.content.PageContentFactory;

import javax.inject.Inject;

public class HomePageContentFactory extends PageContentFactory<HomePageContent, Void> {

    private final PageTitleResolver pageTitleResolver;

    @Inject
    public HomePageContentFactory(final PageTitleResolver pageTitleResolver) {
        this.pageTitleResolver = pageTitleResolver;
    }

    protected final PageTitleResolver getPageTitleResolver() {
        return pageTitleResolver;
    }

    @Override
    protected HomePageContent newViewModelInstance(final Void input) {
        return new HomePageContent();
    }

    @Override
    public final HomePageContent create(final Void input) {
        return super.create(input);
    }

    public final HomePageContent create() {
        return super.create(null);
    }

    @Override
    protected final void initialize(final HomePageContent viewModel, final Void input) {
        super.initialize(viewModel, input);
    }

    @Override
    protected void fillTitle(final HomePageContent viewModel, final Void input) {
        viewModel.setTitle(pageTitleResolver.getOrEmpty("catalog:home.title"));
    }
}
