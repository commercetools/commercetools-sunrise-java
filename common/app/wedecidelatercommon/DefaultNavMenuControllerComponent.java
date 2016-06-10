package wedecidelatercommon;

import com.commercetools.sunrise.common.controllers.SunrisePageData;
import com.commercetools.sunrise.common.hooks.SunrisePageDataHook;
import com.commercetools.sunrise.common.models.NavMenuDataFactory;
import framework.ControllerComponent;

import javax.inject.Inject;

public class DefaultNavMenuControllerComponent implements ControllerComponent, SunrisePageDataHook {
    @Inject
    private NavMenuDataFactory navMenuDataFactory;

    @Override
    public void acceptSunrisePageData(final SunrisePageData sunrisePageData) {
        sunrisePageData.getHeader().setNavMenu(navMenuDataFactory.create());
    }
}
