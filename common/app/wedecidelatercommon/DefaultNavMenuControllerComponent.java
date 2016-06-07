package wedecidelatercommon;

import common.controllers.SunrisePageData;
import common.hooks.SunrisePageDataHook;
import common.models.NavMenuDataFactory;
import framework.ControllerSunriseComponent;

import javax.inject.Inject;

public class DefaultNavMenuControllerComponent implements ControllerSunriseComponent, SunrisePageDataHook {
    @Inject
    private NavMenuDataFactory navMenuDataFactory;

    @Override
    public void acceptSunrisePageData(final SunrisePageData sunrisePageData) {
        sunrisePageData.getHeader().setNavMenu(navMenuDataFactory.create());
    }
}
