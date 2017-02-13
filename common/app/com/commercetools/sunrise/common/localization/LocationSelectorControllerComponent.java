package com.commercetools.sunrise.common.localization;

import com.commercetools.sunrise.framework.ControllerComponent;
import com.commercetools.sunrise.hooks.consumers.PageDataReadyHook;

import javax.inject.Inject;

public class LocationSelectorControllerComponent implements ControllerComponent, PageDataReadyHook {

    private final LocalizationSelectorBeanFactory localizationSelectorBeanFactory;

    @Inject
    public LocationSelectorControllerComponent(final LocalizationSelectorBeanFactory localizationSelectorBeanFactory) {
        this.localizationSelectorBeanFactory = localizationSelectorBeanFactory;
    }

    @Override
    public void onPageDataReady(final PageData pageData) {
        pageData.getHeader().setLocation(localizationSelectorBeanFactory.create());
    }
}
