package com.commercetools.sunrise.common.reverserouter;

import com.commercetools.sunrise.common.pages.ParsedRoutes;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
final class ReflectionMyPersonalDetailsReverseRouter extends ReflectionReverseRouterBase implements MyPersonalDetailsReverseRouter {

    private final ReverseCaller myPersonalDetailsPageCall;
    private final ReverseCaller myPersonalDetailsProcessFormCall;

    @Inject
    private ReflectionMyPersonalDetailsReverseRouter(final ParsedRoutes parsedRoutes) {
        myPersonalDetailsPageCall = getCallerForRoute(parsedRoutes, "myPersonalDetailsPageCall");
        myPersonalDetailsProcessFormCall = getCallerForRoute(parsedRoutes, "myPersonalDetailsProcessFormCall");
    }

    @Override
    public Call myPersonalDetailsPageCall(final String languageTag) {
        return myPersonalDetailsPageCall.call(languageTag);
    }

    @Override
    public Call myPersonalDetailsProcessFormCall(final String languageTag) {
        return myPersonalDetailsProcessFormCall.call(languageTag);
    }
}
