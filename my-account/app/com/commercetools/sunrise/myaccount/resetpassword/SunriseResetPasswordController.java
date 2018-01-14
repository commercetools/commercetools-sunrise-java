package com.commercetools.sunrise.myaccount.resetpassword;

import com.commercetools.sunrise.core.SunriseController;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.core.viewmodels.PageData;
import play.mvc.Result;
import play.mvc.Results;

import java.util.concurrent.CompletionStage;

/**
 * This controller performs the reset of a customer's password.
 *
 * It shows a form to enter and confirm the new password {@link #showResetPassword(String)}.
 *
 * It processes the form and sends a password reset command to the commercetools platform {@link #processResetPassword()}.
 */
public abstract class SunriseResetPasswordController extends SunriseController {

    private final TemplateEngine templateEngine;
    private final ResetPasswordFormAction resetPasswordFormAction;
    private final ResetPasswordRequestFormAction resetPasswordRequestFormAction;

    public SunriseResetPasswordController(final TemplateEngine templateEngine,
                                          final ResetPasswordFormAction resetPasswordFormAction,
                                          final ResetPasswordRequestFormAction resetPasswordRequestFormAction) {
        this.templateEngine = templateEngine;
        this.resetPasswordFormAction = resetPasswordFormAction;
        this.resetPasswordRequestFormAction = resetPasswordRequestFormAction;
    }

    @EnableHooks
    public CompletionStage<Result> showRequestResetPasswordForm() {
        return templateEngine.render("my-account-forgot-password")
                .thenApply(Results::ok);
    }

    @EnableHooks
    public CompletionStage<Result> showResetPasswordForm(final String passwordToken) {
        final PageData pageData = PageData.of().put("passwordToken", passwordToken);
        return templateEngine.render("my-account-reset-password", pageData)
                .thenApply(Results::ok);
    }

    @EnableHooks
    public CompletionStage<Result> requestResetPassword() {
        return resetPasswordRequestFormAction.apply(this::onPasswordResetRequested,
                form -> {
                    final PageData pageData = PageData.of().put("resetPasswordRequestForm", form);
                    return templateEngine.render("my-account-forgot-password", pageData)
                            .thenApply(Results::badRequest);
                });
    }

    @EnableHooks
    public CompletionStage<Result> resetPassword() {
        return resetPasswordFormAction.apply(this::onPasswordReset,
                form -> {
                    final PageData pageData = PageData.of().put("resetPasswordForm", form);
                    return templateEngine.render("my-account-reset-password", pageData)
                            .thenApply(Results::badRequest);
                });
    }

    protected abstract Result onPasswordResetRequested();

    protected abstract Result onPasswordReset();
}
