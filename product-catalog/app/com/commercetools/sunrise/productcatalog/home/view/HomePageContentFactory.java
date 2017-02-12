package com.commercetools.sunrise.productcatalog.home.view;

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
    public final HomePageContent create(final Void data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final HomePageContent model, final Void data) {
        super.initialize(model, data);
    }

    @Override
    protected void fillTitle(final HomePageContent model, final Void data) {
        model.setTitle(pageTitleResolver.getOrEmpty("catalog:home.title"));
    }
}
