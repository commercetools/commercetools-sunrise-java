package com.commercetools.sunrise.core.reverserouters.common.localization;

import com.commercetools.sunrise.core.reverserouters.AbstractReflectionReverseRouter;
import com.commercetools.sunrise.core.reverserouters.ParsedRoutes;
import com.commercetools.sunrise.core.reverserouters.ReverseCaller;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DefaultLocalizationReverseRouter extends AbstractReflectionReverseRouter implements LocalizationReverseRouter {

    private final ReverseCaller changeLanguageProcessCaller;
    private final ReverseCaller changeCountryProcessCaller;

    @Inject
    protected DefaultLocalizationReverseRouter(final ParsedRoutes parsedRoutes) {
        changeLanguageProcessCaller = getReverseCallerForSunriseRoute(CHANGE_LANGUAGE_PROCESS, parsedRoutes);
        changeCountryProcessCaller = getReverseCallerForSunriseRoute(CHANGE_COUNTRY_PROCESS, parsedRoutes);
    }

    @Override
    public Call changeLanguageProcessCall() {
        return changeLanguageProcessCaller.call();
    }

    @Override
    public Call changeCountryProcessCall() {
        return changeCountryProcessCaller.call();
    }
}
