package com.commercetools.sunrise.core.reverserouters.myaccount.recoverpassword;

import com.commercetools.sunrise.core.reverserouters.AbstractReflectionReverseRouter;
import com.commercetools.sunrise.core.reverserouters.ParsedRoutes;
import com.commercetools.sunrise.core.reverserouters.ReverseCaller;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DefaultRecoverPasswordReverseRouter extends AbstractReflectionReverseRouter implements RecoverPasswordReverseRouter {

    private final ReverseCaller resetPasswordPageCaller;
    private final ReverseCaller resetPasswordProcessCaller;
    private final ReverseCaller requestRecoveryEmailPageCaller;
    private final ReverseCaller requestRecoveryEmailProcessCaller;

    @Inject
    protected DefaultRecoverPasswordReverseRouter(final ParsedRoutes parsedRoutes) {
        resetPasswordPageCaller = getReverseCallerForSunriseRoute(RESET_PASSWORD_PAGE, parsedRoutes);
        resetPasswordProcessCaller = getReverseCallerForSunriseRoute(RESET_PASSWORD_PROCESS, parsedRoutes);
        requestRecoveryEmailPageCaller = getReverseCallerForSunriseRoute(REQUEST_RECOVERY_EMAIL_PAGE,
                parsedRoutes);
        requestRecoveryEmailProcessCaller = getReverseCallerForSunriseRoute(REQUEST_RECOVERY_EMAIL_PROCESS,
                parsedRoutes);
    }

    @Override
    public Call resetPasswordPageCall(final String resetToken) {
        return resetPasswordPageCaller.call(resetToken);
    }

    @Override
    public Call resetPasswordProcessCall(final String resetToken) {
        return resetPasswordProcessCaller.call(resetToken);
    }

    @Override
    public Call requestRecoveryEmailPageCall() {
        return requestRecoveryEmailPageCaller.call();
    }

    @Override
    public Call requestRecoveryEmailProcessCall() {
        return requestRecoveryEmailProcessCaller.call();
    }
}
