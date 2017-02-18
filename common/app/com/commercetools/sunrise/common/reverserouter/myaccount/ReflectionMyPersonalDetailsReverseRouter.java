package com.commercetools.sunrise.common.reverserouter.myaccount;

import com.commercetools.sunrise.common.reverserouter.AbstractReflectionReverseRouter;
import com.commercetools.sunrise.common.reverserouter.ReverseCaller;
import com.commercetools.sunrise.framework.ParsedRouteList;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
final class ReflectionMyPersonalDetailsReverseRouter extends AbstractReflectionReverseRouter implements MyPersonalDetailsSimpleReverseRouter {

    private final ReverseCaller myPersonalDetailsPageCall;
    private final ReverseCaller myPersonalDetailsProcessFormCall;

    @Inject
    private ReflectionMyPersonalDetailsReverseRouter(final ParsedRouteList parsedRouteList) {
        myPersonalDetailsPageCall = getCallerForRoute(parsedRouteList, "myPersonalDetailsPageCall");
        myPersonalDetailsProcessFormCall = getCallerForRoute(parsedRouteList, "myPersonalDetailsProcessFormCall");
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
