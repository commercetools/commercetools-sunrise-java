package controllers;

import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.core.viewmodels.content.messages.MessageType;
import com.commercetools.sunrise.myaccount.resetpassword.ResetPasswordFormAction;
import com.commercetools.sunrise.myaccount.resetpassword.ResetPasswordRequestFormAction;
import com.commercetools.sunrise.myaccount.resetpassword.SunriseResetPasswordController;
import controllers.myaccount.routes;
import play.mvc.Result;

import javax.inject.Inject;

@LogMetrics
@NoCache
public final class ResetPasswordController extends SunriseResetPasswordController {

    @Inject
    public ResetPasswordController(final TemplateEngine templateEngine,
                                   final ResetPasswordFormAction resetPasswordFormAction,
                                   final ResetPasswordRequestFormAction resetPasswordRequestFormAction) {
        super(templateEngine, resetPasswordFormAction, resetPasswordRequestFormAction);
    }

    @Override
    protected Result onPasswordResetRequested() {
        saveMessage(MessageType.SUCCESS, "my-account.forgotPassword.feedbackMessage");
        return redirect(controllers.myaccount.routes.ResetPasswordController.showResetPasswordRequest());
    }

    @Override
    protected Result onPasswordReset() {
        return redirect(routes.ResetPasswordController.showResetPassword());
    }
}
