package common.hooks;

import common.controllers.SunrisePageData;

public interface SunrisePageDataHook extends Hook {
    void acceptSunrisePageData(final SunrisePageData sunrisePageData);
}
