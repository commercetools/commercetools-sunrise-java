package com.commercetools.sunrise.common.pages;

import com.commercetools.sunrise.framework.components.ControllerComponent;
import com.commercetools.sunrise.framework.hooks.consumers.PageDataReadyHook;

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
