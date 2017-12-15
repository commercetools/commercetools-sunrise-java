package com.commercetools.sunrise.framework.reverserouters.myaccount.authentication;

import com.commercetools.sunrise.framework.reverserouters.AbstractReflectionReverseRouter;
import com.commercetools.sunrise.framework.reverserouters.ParsedRoutes;
import com.commercetools.sunrise.framework.reverserouters.ReverseCaller;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DefaultAuthenticationReverseRouter extends AbstractReflectionReverseRouter implements AuthenticationReverseRouter {

    private final ReverseCaller logInPageCaller;
    private final ReverseCaller logInProcessCaller;
    private final ReverseCaller signUpProcessCaller;
    private final ReverseCaller logOutProcessCaller;

    @Inject
    protected DefaultAuthenticationReverseRouter(final ParsedRoutes parsedRoutes) {
        logInPageCaller = getReverseCallerForSunriseRoute(LOG_IN_PAGE, parsedRoutes);
        logInProcessCaller = getReverseCallerForSunriseRoute(LOG_IN_PROCESS, parsedRoutes);
        signUpProcessCaller = getReverseCallerForSunriseRoute(SIGN_UP_PROCESS, parsedRoutes);
        logOutProcessCaller = getReverseCallerForSunriseRoute(LOG_OUT_PROCESS, parsedRoutes);
    }

    @Override
    public Call logInPageCall() {
        return logInPageCaller.call();
    }

    @Override
    public Call logInProcessCall() {
        return logInProcessCaller.call();
    }

    @Override
    public Call signUpProcessCall() {
        return signUpProcessCaller.call();
    }

    @Override
    public Call logOutProcessCall() {
        return logOutProcessCaller.call();
    }
}
