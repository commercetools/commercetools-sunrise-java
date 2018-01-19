package controllers;

import com.commercetools.sunrise.controllers.cache.NoCache;
import com.commercetools.sunrise.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.SunriseController;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.core.viewmodels.PageData;
import com.commercetools.sunrise.core.viewmodels.content.messages.MessageType;
import com.commercetools.sunrise.myaccount.resetpassword.ResetPasswordFormAction;
import com.commercetools.sunrise.myaccount.resetpassword.ResetPasswordRequestFormAction;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@LogMetrics
@NoCache
public class ResetPasswordController extends SunriseController {

    private static final String REQUEST_RESET_PASSWORD_TEMPLATE = "my-account-forgot-password";
    private static final String RESET_PASSWORD_TEMPLATE = "my-account-reset-password";

    private final TemplateEngine templateEngine;
    private final ResetPasswordFormAction resetPasswordFormAction;
    private final ResetPasswordRequestFormAction resetPasswordRequestFormAction;

    @Inject
    ResetPasswordController(final TemplateEngine templateEngine,
                            final ResetPasswordFormAction resetPasswordFormAction,
                            final ResetPasswordRequestFormAction resetPasswordRequestFormAction) {
        this.templateEngine = templateEngine;
        this.resetPasswordFormAction = resetPasswordFormAction;
        this.resetPasswordRequestFormAction = resetPasswordRequestFormAction;
    }

    @EnableHooks
    public CompletionStage<Result> showRequestResetPasswordForm() {
        return templateEngine.render(REQUEST_RESET_PASSWORD_TEMPLATE).thenApply(Results::ok);
    }

    @EnableHooks
    public CompletionStage<Result> requestResetPassword() {
        return resetPasswordRequestFormAction.apply(
                () -> routes.ResetPasswordController.showRequestResetPasswordForm(),
                form -> templateEngine.render(REQUEST_RESET_PASSWORD_TEMPLATE, PageData.of().put("resetPasswordRequestForm", form)));
    }

    @EnableHooks
    public CompletionStage<Result> showResetPasswordForm(final String passwordToken) {
        final PageData pageData = PageData.of().put("passwordToken", passwordToken);
        return templateEngine.render(RESET_PASSWORD_TEMPLATE, pageData).thenApply(Results::ok);
    }

    @EnableHooks
    public CompletionStage<Result> resetPassword() {
        return resetPasswordFormAction.apply(
                () -> routes.AuthenticationController.show(),
                form -> templateEngine.render(RESET_PASSWORD_TEMPLATE, PageData.of().put("resetPasswordForm", form)));
    }

    @Override
    protected Result onPasswordResetRequested() {
        saveMessage(MessageType.SUCCESS, "my-account.forgotPassword.feedbackMessage");
        return redirect(routes.ResetPasswordController.showRequestResetPasswordForm());
    }
}
