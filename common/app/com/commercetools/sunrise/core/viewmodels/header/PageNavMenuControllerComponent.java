package com.commercetools.sunrise.core.viewmodels.header;

import com.commercetools.sunrise.core.components.controllers.ControllerComponent;
import com.commercetools.sunrise.core.hooks.application.PageDataReadyHook;
import com.commercetools.sunrise.core.viewmodels.PageData;

import javax.inject.Inject;

public final class PageNavMenuControllerComponent implements ControllerComponent, PageDataReadyHook {

    private PageNavMenuFactory pageNavMenuFactory;

    @Inject
    public PageNavMenuControllerComponent(final PageNavMenuFactory pageNavMenuFactory) {
        this.pageNavMenuFactory = pageNavMenuFactory;
    }

    @Override
    public void onPageDataReady(final PageData pageData) {
        pageData.getHeader().setNavMenu(pageNavMenuFactory.create(null));
    }
}
