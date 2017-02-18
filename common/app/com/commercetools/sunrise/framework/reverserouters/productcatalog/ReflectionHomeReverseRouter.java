package com.commercetools.sunrise.framework.reverserouters.productcatalog;

import com.commercetools.sunrise.framework.reverserouters.AbstractReflectionReverseRouter;
import com.commercetools.sunrise.framework.reverserouters.ParsedRouteList;
import com.commercetools.sunrise.framework.reverserouters.ReverseCaller;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
final class ReflectionHomeReverseRouter extends AbstractReflectionReverseRouter implements HomeSimpleReverseRouter {

    private final ReverseCaller homePageCaller;

    @Inject
    private ReflectionHomeReverseRouter(final ParsedRouteList parsedRouteList) {
        homePageCaller = getCallerForRoute(parsedRouteList, HOME_PAGE);
    }

    @Override
    public Call homePageCall(final String languageTag) {
        return homePageCaller.call(languageTag);
    }
}
