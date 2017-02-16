package com.commercetools.sunrise.common.pages;

import com.commercetools.sunrise.framework.ControllerComponent;
import com.commercetools.sunrise.hooks.consumers.PageDataReadyHook;

import javax.inject.Inject;

public final class DefaultPageNavMenuControllerComponent implements ControllerComponent, PageDataReadyHook {

    private PageNavMenuFactory pageNavMenuFactory;

    @Inject
    public DefaultPageNavMenuControllerComponent(final PageNavMenuFactory pageNavMenuFactory) {
        this.pageNavMenuFactory = pageNavMenuFactory;
    }

    @Override
    public void onPageDataReady(final PageData pageData) {
        pageData.getHeader().setNavMenu(pageNavMenuFactory.create(null));
    }
}
