package com.commercetools.sunrise.framework.reverserouters.common;

import com.commercetools.sunrise.framework.reverserouters.AbstractReflectionReverseRouter;
import com.commercetools.sunrise.framework.reverserouters.ParsedRouteList;
import com.commercetools.sunrise.framework.reverserouters.ReverseCaller;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
final class ReflectionLocalizationReverseRouter extends AbstractReflectionReverseRouter implements LocalizationSimpleReverseRouter {

    private final ReverseCaller changeLanguageProcessCaller;
    private final ReverseCaller changeCountryProcessCaller;

    @Inject
    private ReflectionLocalizationReverseRouter(final ParsedRouteList parsedRouteList) {
        changeLanguageProcessCaller = getCallerForRoute(parsedRouteList, CHANGE_LANGUAGE_PROCESS);
        changeCountryProcessCaller = getCallerForRoute(parsedRouteList, CHANGE_COUNTRY_PROCESS);
    }


    @Override
    public Call changeLanguageProcessCall() {
        return changeLanguageProcessCaller.call();
    }

    @Override
    public Call changeCountryProcessCall(final String languageTag) {
        return changeCountryProcessCaller.call(languageTag);
    }
}
