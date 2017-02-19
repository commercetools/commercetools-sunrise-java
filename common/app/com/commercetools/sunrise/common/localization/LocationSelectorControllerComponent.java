package com.commercetools.sunrise.common.localization;

import com.commercetools.sunrise.common.models.LocalizationSelectorBeanFactory;
import com.commercetools.sunrise.common.pages.PageData;
import com.commercetools.sunrise.framework.components.ControllerComponent;
import com.commercetools.sunrise.framework.hooks.consumers.PageDataReadyHook;

import javax.inject.Inject;

public class LocationSelectorControllerComponent implements ControllerComponent, PageDataReadyHook {

    private final LocalizationSelectorBeanFactory localizationSelectorBeanFactory;

    @Inject
    public LocationSelectorControllerComponent(final LocalizationSelectorBeanFactory localizationSelectorBeanFactory) {
        this.localizationSelectorBeanFactory = localizationSelectorBeanFactory;
    }

    @Override
    public void onPageDataReady(final PageData pageData) {
        pageData.getHeader().setLocation(localizationSelectorBeanFactory.create(null));
    }
}
