package com.commercetools.sunrise.framework.viewmodels.header;

import com.commercetools.sunrise.framework.components.controllers.ControllerComponent;
import com.commercetools.sunrise.framework.hooks.application.PageDataReadyHook;
import com.commercetools.sunrise.framework.viewmodels.PageData;

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
