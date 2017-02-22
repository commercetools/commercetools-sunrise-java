package com.commercetools.sunrise.productcatalog.home.viewmodels;

import com.commercetools.sunrise.common.models.PageContentFactory;
import com.commercetools.sunrise.common.utils.PageTitleResolver;

import javax.inject.Inject;

public class HomePageContentFactory extends PageContentFactory<HomePageContent, Void> {

    private final PageTitleResolver pageTitleResolver;

    @Inject
    public HomePageContentFactory(final PageTitleResolver pageTitleResolver) {
        this.pageTitleResolver = pageTitleResolver;
    }

    @Override
    protected HomePageContent getViewModelInstance() {
        return new HomePageContent();
    }

    @Override
    public final HomePageContent create(final Void input) {
        return super.create(input);
    }

    @Override
    protected final void initialize(final HomePageContent viewModel, final Void input) {
        super.initialize(viewModel, input);
    }

    @Override
    protected void fillTitle(final HomePageContent model, final Void input) {
        model.setTitle(pageTitleResolver.getOrEmpty("catalog:home.title"));
    }
}
