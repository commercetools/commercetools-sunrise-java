package com.commercetools.sunrise.common.pages;

import com.commercetools.sunrise.framework.ControllerComponent;
import com.commercetools.sunrise.hooks.consumers.PageDataHook;

import javax.inject.Inject;

public class DefaultPageNavMenuControllerComponent implements ControllerComponent, PageDataHook {

    @Inject
    private PageNavMenuFactory pageNavMenuFactory;

    @Override
    public void onPageDataCreated(final PageData pageData) {
        pageData.getHeader().setNavMenu(pageNavMenuFactory.create());
    }
}
