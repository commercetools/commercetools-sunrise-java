package com.commercetools.sunrise.framework.reverserouters.productcatalog.home;

import com.commercetools.sunrise.framework.reverserouters.AbstractReflectionReverseRouter;
import com.commercetools.sunrise.framework.reverserouters.ParsedRoutes;
import com.commercetools.sunrise.framework.reverserouters.ReverseCaller;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DefaultHomeReverseRouter extends AbstractReflectionReverseRouter implements HomeReverseRouter {

    private final ReverseCaller homePageCaller;

    @Inject
    protected DefaultHomeReverseRouter(final ParsedRoutes parsedRoutes) {
        homePageCaller = getReverseCallerForSunriseRoute(HOME_PAGE, parsedRoutes);
    }

    @Override
    public Call homePageCall() {
        return homePageCaller.call();
    }
}
