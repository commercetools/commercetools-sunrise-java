package com.commercetools.sunrise.framework.reverserouters.myaccount.recoverpassword;

import com.commercetools.sunrise.framework.reverserouters.AbstractReflectionReverseRouter;
import com.commercetools.sunrise.framework.reverserouters.ParsedRoutes;
import com.commercetools.sunrise.framework.reverserouters.ReverseCaller;
import play.mvc.Call;

import javax.inject.Inject;


final class SimpleRecoverPasswordReverseRouterByReflection extends AbstractReflectionReverseRouter implements SimpleRecoverPasswordReverseRouter {
    private final ReverseCaller resetPasswordPageCaller;
    private final ReverseCaller resetPasswordProcessCaller;
    private final ReverseCaller requestRecoveryEmailPageCaller;
    private final ReverseCaller requestRecoveryEmailProcessCaller;

    @Inject
    public SimpleRecoverPasswordReverseRouterByReflection(final ParsedRoutes parsedRoutes) {
        resetPasswordPageCaller = getReverseCallerForSunriseRoute(RESET_PASSWORD_PAGE, parsedRoutes);
        resetPasswordProcessCaller = getReverseCallerForSunriseRoute(RESET_PASSWORD_PROCESS, parsedRoutes);
        requestRecoveryEmailPageCaller = getReverseCallerForSunriseRoute(REQUEST_RECOVERY_EMAIL_PAGE,
                parsedRoutes);
        requestRecoveryEmailProcessCaller = getReverseCallerForSunriseRoute(REQUEST_RECOVERY_EMAIL_PROCESS,
                parsedRoutes);
    }

    @Override
    public Call resetPasswordPageCall(final String languageTag, final String resetToken) {
        return resetPasswordPageCaller.call(languageTag, resetToken);
    }

    @Override
    public Call resetPasswordProcessCall(final String languageTag, final String resetToken) {
        return resetPasswordProcessCaller.call(languageTag, resetToken);
    }

    @Override
    public Call requestRecoveryEmailPageCall(final String languageTag) {
        return requestRecoveryEmailPageCaller.call(languageTag);
    }

    @Override
    public Call requestRecoveryEmailProcessCall(final String languageTag) {
        return requestRecoveryEmailProcessCaller.call(languageTag);
    }
}
