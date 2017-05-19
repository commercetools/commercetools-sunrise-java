package com.commercetools.sunrise.framework.reverserouters.myaccount.changepassword;

import com.commercetools.sunrise.framework.reverserouters.AbstractReflectionReverseRouter;
import com.commercetools.sunrise.framework.reverserouters.ParsedRoutes;
import com.commercetools.sunrise.framework.reverserouters.ReverseCaller;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
final class SimpleChangePasswordReverseRouterByReflection extends AbstractReflectionReverseRouter implements SimpleChangePasswordReverseRouter {

    private final ReverseCaller changePasswordProcessCaller;

    @Inject
    private SimpleChangePasswordReverseRouterByReflection(final ParsedRoutes parsedRoutes) {
        changePasswordProcessCaller = getReverseCallerForSunriseRoute(CHANGE_PASSWORD_PROCESS, parsedRoutes);
    }

    @Override
    public Call changePasswordProcessCall(final String languageTag) {
        return changePasswordProcessCaller.call(languageTag);
    }
}
