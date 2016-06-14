package com.commercetools.sunrise.wedecidelatercommon;

import com.commercetools.sunrise.common.contexts.ProjectContext;
import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.hooks.SunrisePageDataHook;
import com.commercetools.sunrise.common.models.LocationSelector;
import com.commercetools.sunrise.common.pages.SunrisePageData;
import com.commercetools.sunrise.framework.ControllerComponent;

import javax.inject.Inject;

public class LocationSelectorControllerComponent implements ControllerComponent, SunrisePageDataHook {
    @Inject
    private UserContext userContext;
    @Inject
    private ProjectContext projectContext;

    @Override
    public void acceptSunrisePageData(final SunrisePageData sunrisePageData) {
        sunrisePageData.getHeader().setLocation(new LocationSelector(projectContext, userContext));
    }
}
