package com.commercetools.sunrise.core.reverserouters.myaccount.changepassword;

import com.commercetools.sunrise.core.reverserouters.AbstractReflectionReverseRouter;
import com.commercetools.sunrise.core.reverserouters.ParsedRoutes;
import com.commercetools.sunrise.core.reverserouters.ReverseCaller;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DefaultChangePasswordReverseRouter extends AbstractReflectionReverseRouter implements ChangePasswordReverseRouter {

    private final ReverseCaller changePasswordPageCaller;
    private final ReverseCaller changePasswordProcessCaller;

    @Inject
    protected DefaultChangePasswordReverseRouter(final ParsedRoutes parsedRoutes) {
        changePasswordPageCaller = getReverseCallerForSunriseRoute(CHANGE_PASSWORD_PAGE, parsedRoutes);
        changePasswordProcessCaller = getReverseCallerForSunriseRoute(CHANGE_PASSWORD_PROCESS, parsedRoutes);
    }

    @Override
    public Call changePasswordPageCall() {
        return changePasswordPageCaller.call();
    }

    @Override
    public Call changePasswordProcessCall() {
        return changePasswordProcessCaller.call();
    }
}
