package com.commercetools.sunrise.common.pages;

import com.commercetools.sunrise.common.pages.SunrisePageData;
import com.commercetools.sunrise.common.hooks.SunrisePageDataHook;
import com.commercetools.sunrise.common.pages.PageNavMenuFactory;
import com.commercetools.sunrise.framework.ControllerComponent;

import javax.inject.Inject;

public class DefaultPageNavMenuControllerComponent implements ControllerComponent, SunrisePageDataHook {

    @Inject
    private PageNavMenuFactory pageNavMenuFactory;

    @Override
    public void acceptSunrisePageData(final SunrisePageData sunrisePageData) {
        sunrisePageData.getHeader().setNavMenu(pageNavMenuFactory.create());
    }
}
