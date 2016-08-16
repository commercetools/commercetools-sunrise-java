package com.commercetools.sunrise.common.reverserouter;

import com.commercetools.sunrise.common.pages.ParsedRoutes;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
final class ReflectionHomeReverseRouter extends ReflectionReverseRouterBase implements HomeReverseRouter {

    private ReverseCaller homePageCaller;

    @Inject
    private void setRoutes(final ParsedRoutes parsedRoutes) {
        homePageCaller = getCallerForRoute(parsedRoutes, "homePageCall");
    }

    @Override
    public Call homePageCall(final String languageTag) {
        return homePageCaller.call(languageTag);
    }
}
