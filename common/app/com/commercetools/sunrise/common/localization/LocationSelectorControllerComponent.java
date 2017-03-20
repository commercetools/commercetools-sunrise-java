package com.commercetools.sunrise.common.localization;

import com.commercetools.sunrise.framework.viewmodels.PageData;
import com.commercetools.sunrise.framework.components.controllers.ControllerComponent;
import com.commercetools.sunrise.framework.hooks.application.PageDataReadyHook;

import javax.inject.Inject;

public class LocationSelectorControllerComponent implements ControllerComponent, PageDataReadyHook {

    private final LocalizationSelectorViewModelFactory localizationSelectorViewModelFactory;

    @Inject
    public LocationSelectorControllerComponent(final LocalizationSelectorViewModelFactory localizationSelectorViewModelFactory) {
        this.localizationSelectorViewModelFactory = localizationSelectorViewModelFactory;
    }

    @Override
    public void onPageDataReady(final PageData pageData) {
        pageData.getHeader().setLocation(localizationSelectorViewModelFactory.create(null));
    }
}
