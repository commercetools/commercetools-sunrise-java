package com.commercetools.sunrise.framework.reverserouters.myaccount;

import com.commercetools.sunrise.framework.reverserouters.AbstractReflectionReverseRouter;
import com.commercetools.sunrise.framework.reverserouters.ParsedRouteList;
import com.commercetools.sunrise.framework.reverserouters.ReverseCaller;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
final class ReflectionAuthenticationReverseRouter extends AbstractReflectionReverseRouter implements AuthenticationSimpleReverseRouter {

    private final ReverseCaller logInPageCaller;
    private final ReverseCaller logInProcessCaller;
    private final ReverseCaller signUpProcessCaller;
    private final ReverseCaller logOutProcessCaller;

    @Inject
    private ReflectionAuthenticationReverseRouter(final ParsedRouteList parsedRouteList) {
        logInPageCaller = getCallerForRoute(parsedRouteList, LOG_IN_PAGE);
        logInProcessCaller = getCallerForRoute(parsedRouteList, LOG_IN_PROCESS);
        signUpProcessCaller = getCallerForRoute(parsedRouteList, SIGN_UP_PROCESS);
        logOutProcessCaller = getCallerForRoute(parsedRouteList, LOG_OUT_PROCESS);
    }

    @Override
    public Call logInPageCall(final String languageTag) {
        return logInPageCaller.call(languageTag);
    }

    @Override
    public Call logInProcessCall(final String languageTag) {
        return logInProcessCaller.call(languageTag);
    }

    @Override
    public Call signUpProcessCall(final String languageTag) {
        return signUpProcessCaller.call(languageTag);
    }

    @Override
    public Call logOutProcessCall(final String languageTag) {
        return logOutProcessCaller.call(languageTag);
    }
}
