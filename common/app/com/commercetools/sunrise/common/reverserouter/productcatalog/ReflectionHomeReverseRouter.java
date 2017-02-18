package com.commercetools.sunrise.common.reverserouter.productcatalog;

import com.commercetools.sunrise.common.reverserouter.AbstractReflectionReverseRouter;
import com.commercetools.sunrise.common.reverserouter.ReverseCaller;
import com.commercetools.sunrise.framework.ParsedRouteList;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
final class ReflectionHomeReverseRouter extends AbstractReflectionReverseRouter implements HomeSimpleReverseRouter {

    private final ReverseCaller homePageCaller;

    @Inject
    private ReflectionHomeReverseRouter(final ParsedRouteList parsedRouteList) {
        homePageCaller = getCallerForRoute(parsedRouteList, "homePageCall");
    }

    @Override
    public Call homePageCall(final String languageTag) {
        return homePageCaller.call(languageTag);
    }
}
