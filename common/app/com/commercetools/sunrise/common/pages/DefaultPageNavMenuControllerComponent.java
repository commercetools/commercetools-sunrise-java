package com.commercetools.sunrise.common.pages;

import com.commercetools.sunrise.framework.ControllerComponent;
import com.commercetools.sunrise.hooks.consumers.PageDataReadyHook;

import javax.inject.Inject;

public class DefaultPageNavMenuControllerComponent implements ControllerComponent, PageDataReadyHook {

    @Inject
    private PageNavMenuFactory pageNavMenuFactory;

    @Override
    public void onPageDataReady(final PageData pageData) {
        pageData.getHeader().setNavMenu(pageNavMenuFactory.create());
    }
}
