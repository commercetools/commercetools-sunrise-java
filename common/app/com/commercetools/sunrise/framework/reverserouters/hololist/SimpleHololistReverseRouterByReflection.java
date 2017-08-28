package com.commercetools.sunrise.framework.reverserouters.hololist;

import com.commercetools.sunrise.framework.reverserouters.AbstractReflectionReverseRouter;
import com.commercetools.sunrise.framework.reverserouters.ParsedRoutes;
import com.commercetools.sunrise.framework.reverserouters.ReverseCaller;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
class SimpleHololistReverseRouterByReflection extends AbstractReflectionReverseRouter implements SimpleHololistReverseRouter {
    private final ReverseCaller addToHololistCaller;
    private final ReverseCaller removeFromHololistCaller;
    private final ReverseCaller clearHololistCaller;
    private final ReverseCaller hololistPageCaller;

    @Inject
    public SimpleHololistReverseRouterByReflection(final ParsedRoutes parsedRoutes) {
        addToHololistCaller = getReverseCallerForSunriseRoute(ADD_TO_HOLOLIST_PROCESS, parsedRoutes);
        removeFromHololistCaller = getReverseCallerForSunriseRoute(REMOVE_FROM_HOLOLIST_PROCESS, parsedRoutes);
        clearHololistCaller = getReverseCallerForSunriseRoute(CLEAR_HOLOLIST_PROCESS, parsedRoutes);
        hololistPageCaller = getReverseCallerForSunriseRoute(HOLOLIST_PAGE, parsedRoutes);
    }

    @Override
    public Call addToHololistProcessCall(final String languageTag) {
        return addToHololistCaller.call(languageTag);
    }

    @Override
    public Call removeFromHololistProcessCall(final String languageTag) {
        return removeFromHololistCaller.call(languageTag);
    }

    @Override
    public Call clearHololistProcessCall(final String languageTag) {
        return clearHololistCaller.call(languageTag);
    }

    @Override
    public Call hololistPageCall(final String languageTag) {
        return hololistPageCaller.call(languageTag);
    }
}