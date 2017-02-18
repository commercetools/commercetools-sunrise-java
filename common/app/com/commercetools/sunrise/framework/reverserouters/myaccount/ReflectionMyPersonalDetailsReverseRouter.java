package com.commercetools.sunrise.framework.reverserouters.myaccount;

import com.commercetools.sunrise.framework.reverserouters.AbstractReflectionReverseRouter;
import com.commercetools.sunrise.framework.reverserouters.ParsedRouteList;
import com.commercetools.sunrise.framework.reverserouters.ReverseCaller;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
final class ReflectionMyPersonalDetailsReverseRouter extends AbstractReflectionReverseRouter implements MyPersonalDetailsSimpleReverseRouter {

    private final ReverseCaller myPersonalDetailsPageCaller;
    private final ReverseCaller myPersonalDetailsProcessCaller;

    @Inject
    private ReflectionMyPersonalDetailsReverseRouter(final ParsedRouteList parsedRouteList) {
        myPersonalDetailsPageCaller = getCallerForRoute(parsedRouteList, MY_PERSONAL_DETAILS_PAGE);
        myPersonalDetailsProcessCaller = getCallerForRoute(parsedRouteList, MY_PERSONAL_DETAILS_PROCESS);
    }

    @Override
    public Call myPersonalDetailsPageCall(final String languageTag) {
        return myPersonalDetailsPageCaller.call(languageTag);
    }

    @Override
    public Call myPersonalDetailsProcessCall(final String languageTag) {
        return myPersonalDetailsProcessCaller.call(languageTag);
    }
}
