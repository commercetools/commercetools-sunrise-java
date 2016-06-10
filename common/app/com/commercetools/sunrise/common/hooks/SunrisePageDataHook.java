package com.commercetools.sunrise.common.hooks;

import com.commercetools.sunrise.common.controllers.SunrisePageData;

public interface SunrisePageDataHook extends Hook {
    void acceptSunrisePageData(final SunrisePageData sunrisePageData);
}
