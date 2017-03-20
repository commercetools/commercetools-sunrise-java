package com.commercetools.sunrise.framework.reverserouters.productcatalog.home;

import com.commercetools.sunrise.framework.reverserouters.AbstractReflectionReverseRouter;
import com.commercetools.sunrise.framework.reverserouters.ParsedRoutes;
import com.commercetools.sunrise.framework.reverserouters.ReverseCaller;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
final class SimpleHomeReverseRouterByReflection extends AbstractReflectionReverseRouter implements SimpleHomeReverseRouter {

    private final ReverseCaller homePageCaller;

    @Inject
    private SimpleHomeReverseRouterByReflection(final ParsedRoutes parsedRoutes) {
        homePageCaller = getReverseCallerForSunriseRoute(HOME_PAGE, parsedRoutes);
    }

    @Override
    public Call homePageCall(final String languageTag) {
        return homePageCaller.call(languageTag);
    }
}
