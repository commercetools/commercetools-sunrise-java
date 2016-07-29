package com.commercetools.sunrise.common.reverserouter;

import com.commercetools.sunrise.common.pages.ParsedRoutes;
import play.mvc.Call;

import javax.inject.Inject;

final class ReflectionMyPersonalDetailsReverseRouter extends ReflectionReverseRouterBase implements MyPersonalDetailsReverseRouter {

    private ReverseCaller myPersonalDetailsPageCall;
    private ReverseCaller myPersonalDetailsProcessFormCall;

    @Inject
    private void setRoutes(final ParsedRoutes parsedRoutes) {
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
