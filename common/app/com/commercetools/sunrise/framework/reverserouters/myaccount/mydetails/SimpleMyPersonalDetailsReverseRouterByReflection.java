package com.commercetools.sunrise.framework.reverserouters.myaccount.mydetails;

import com.commercetools.sunrise.framework.reverserouters.AbstractReflectionReverseRouter;
import com.commercetools.sunrise.framework.reverserouters.ParsedRoutes;
import com.commercetools.sunrise.framework.reverserouters.ReverseCaller;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
final class SimpleMyPersonalDetailsReverseRouterByReflection extends AbstractReflectionReverseRouter implements SimpleMyPersonalDetailsReverseRouter {

    private final ReverseCaller myPersonalDetailsPageCaller;
    private final ReverseCaller myPersonalDetailsProcessCaller;

    @Inject
    private SimpleMyPersonalDetailsReverseRouterByReflection(final ParsedRoutes parsedRoutes) {
        myPersonalDetailsPageCaller = getReverseCallerForSunriseRoute(MY_PERSONAL_DETAILS_PAGE, parsedRoutes);
        myPersonalDetailsProcessCaller = getReverseCallerForSunriseRoute(MY_PERSONAL_DETAILS_PROCESS, parsedRoutes);
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
