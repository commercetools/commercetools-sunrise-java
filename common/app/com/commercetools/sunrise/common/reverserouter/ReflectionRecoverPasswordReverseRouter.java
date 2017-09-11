package com.commercetools.sunrise.common.reverserouter;

import com.commercetools.sunrise.common.pages.ParsedRoutes;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
final class ReflectionRecoverPasswordReverseRouter extends ReflectionReverseRouterBase implements RecoverPasswordReverseRouter {
    private final ReverseCaller resetPasswordPageCaller;
    private final ReverseCaller resetPasswordProcessCaller;
    private final ReverseCaller requestRecoveryEmailPageCaller;
    private final ReverseCaller requestRecoveryEmailProcessCaller;

    @Inject
    private ReflectionRecoverPasswordReverseRouter(final ParsedRoutes parsedRoutes) {
        resetPasswordPageCaller = getCallerForRoute(parsedRoutes, "showResetPasswordForm");
        resetPasswordProcessCaller = getCallerForRoute(parsedRoutes, "processResetPasswordForm");
        requestRecoveryEmailPageCaller = getCallerForRoute(parsedRoutes, "showRecoveryPasswordForm");
        requestRecoveryEmailProcessCaller = getCallerForRoute(parsedRoutes, "processRecoveryPasswordForm");
    }

    @Override
    public Call showResetPasswordForm(final String languageTag, final String resetToken) {
        return resetPasswordPageCaller.call(languageTag, resetToken);
    }

    @Override
    public Call processResetPasswordForm(final String languageTag, final String resetToken) {
        return resetPasswordProcessCaller.call(languageTag, resetToken);
    }

    @Override
    public Call showRequestRecoveryEmailForm(final String languageTag) {
        return requestRecoveryEmailPageCaller.call(languageTag);
    }

    @Override
    public Call processRequestRecoverEmailForm(final String languageTag) {
        return requestRecoveryEmailProcessCaller.call(languageTag);
    }
}
