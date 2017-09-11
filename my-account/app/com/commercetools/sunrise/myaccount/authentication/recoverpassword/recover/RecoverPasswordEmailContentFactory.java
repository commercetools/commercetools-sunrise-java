package com.commercetools.sunrise.myaccount.authentication.recoverpassword.recover;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.reverserouter.RecoverPasswordReverseRouter;
import io.sphere.sdk.customers.CustomerToken;
import play.mvc.Http;

import javax.inject.Inject;

/**
 * Creates the page content to render the password reset email content.
 */
public class RecoverPasswordEmailContentFactory {

    @Inject
    private UserContext userContext;
    @Inject
    private RecoverPasswordReverseRouter recoverPasswordReverseRouter;

    protected final RecoverPasswordReverseRouter getRecoverPasswordReverseRouter() {
        return recoverPasswordReverseRouter;
    }

    protected final UserContext getUserContext() {
        return userContext;
    }

    public RecoverPasswordEmailContent create(final CustomerToken customerToken) {
        final RecoverPasswordEmailContent bean = new RecoverPasswordEmailContent();
        initialize(bean, customerToken);
        return bean;
    }

    protected final void initialize(final RecoverPasswordEmailContent viewModel, final CustomerToken resetPasswordToken) {
        fillPasswordResetUrl(viewModel, resetPasswordToken);
    }

    protected void fillPasswordResetUrl(final RecoverPasswordEmailContent viewModel, final CustomerToken resetPasswordToken) {
        final String passwordResetUrl = createPasswordResetUrl(resetPasswordToken);
        viewModel.setPasswordResetUrl(passwordResetUrl);
    }

    private String createPasswordResetUrl(final CustomerToken resetPasswordToken) {
        final Http.Request request = Http.Context.current().request();
        return recoverPasswordReverseRouter.showResetPasswordForm(userContext.languageTag(), resetPasswordToken.getValue())
                .absoluteURL(request);
    }
}