package wedecidelatercommon;

import common.contexts.ProjectContext;
import common.contexts.UserContext;
import common.controllers.SunrisePageData;
import common.hooks.SunrisePageDataHook;
import common.models.LocationSelector;
import framework.ControllerComponent;

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
