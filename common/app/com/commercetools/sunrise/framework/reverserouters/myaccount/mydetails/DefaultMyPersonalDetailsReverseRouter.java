package com.commercetools.sunrise.framework.reverserouters.myaccount.mydetails;

import com.commercetools.sunrise.framework.reverserouters.AbstractReflectionReverseRouter;
import com.commercetools.sunrise.framework.reverserouters.ParsedRoutes;
import com.commercetools.sunrise.framework.reverserouters.ReverseCaller;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DefaultMyPersonalDetailsReverseRouter extends AbstractReflectionReverseRouter implements MyPersonalDetailsReverseRouter {

    private final ReverseCaller myPersonalDetailsPageCaller;
    private final ReverseCaller myPersonalDetailsProcessCaller;

    @Inject
    protected DefaultMyPersonalDetailsReverseRouter(final ParsedRoutes parsedRoutes) {
        myPersonalDetailsPageCaller = getReverseCallerForSunriseRoute(MY_PERSONAL_DETAILS_PAGE, parsedRoutes);
        myPersonalDetailsProcessCaller = getReverseCallerForSunriseRoute(MY_PERSONAL_DETAILS_PROCESS, parsedRoutes);
    }

    @Override
    public Call myPersonalDetailsPageCall() {
        return myPersonalDetailsPageCaller.call();
    }

    @Override
    public Call myPersonalDetailsProcessCall() {
        return myPersonalDetailsProcessCaller.call();
    }
}
