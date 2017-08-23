package com.commercetools.sunrise.framework.reverserouters.myaccount.authentication;

import com.commercetools.sunrise.framework.reverserouters.AbstractReflectionReverseRouter;
import com.commercetools.sunrise.framework.reverserouters.ParsedRoutes;
import com.commercetools.sunrise.framework.reverserouters.ReverseCaller;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
final class SimpleAuthenticationReverseRouterByReflection extends AbstractReflectionReverseRouter implements SimpleAuthenticationReverseRouter {
    private final ReverseCaller logInPageCaller;
    private final ReverseCaller logInProcessCaller;
    private final ReverseCaller signUpProcessCaller;
    private final ReverseCaller logOutProcessCaller;

    @Inject
    private SimpleAuthenticationReverseRouterByReflection(final ParsedRoutes parsedRoutes) {
        logInPageCaller = getReverseCallerForSunriseRoute(LOG_IN_PAGE, parsedRoutes);
        logInProcessCaller = getReverseCallerForSunriseRoute(LOG_IN_PROCESS, parsedRoutes);
        signUpProcessCaller = getReverseCallerForSunriseRoute(SIGN_UP_PROCESS, parsedRoutes);
        logOutProcessCaller = getReverseCallerForSunriseRoute(LOG_OUT_PROCESS, parsedRoutes);
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
