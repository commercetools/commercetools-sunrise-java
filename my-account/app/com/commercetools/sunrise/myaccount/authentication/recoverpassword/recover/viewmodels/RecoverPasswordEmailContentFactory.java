package com.commercetools.sunrise.myaccount.authentication.recoverpassword.recover.viewmodels;

import com.commercetools.sunrise.framework.reverserouters.myaccount.recoverpassword.RecoverPasswordReverseRouter;
import com.commercetools.sunrise.framework.viewmodels.SimpleViewModelFactory;
import io.sphere.sdk.customers.CustomerToken;
import play.mvc.Http;

import javax.inject.Inject;

/**
 * Creates the page content to render the password reset email content.
 */
public class RecoverPasswordEmailContentFactory extends SimpleViewModelFactory<RecoverPasswordEmailContent, CustomerToken> {

    private final RecoverPasswordReverseRouter recoverPasswordReverseRouter;

    @Inject
    protected RecoverPasswordEmailContentFactory(final RecoverPasswordReverseRouter recoverPasswordReverseRouter) {
        this.recoverPasswordReverseRouter = recoverPasswordReverseRouter;
    }

    protected final RecoverPasswordReverseRouter getRecoverPasswordReverseRouter() {
        return recoverPasswordReverseRouter;
    }

    @Override
    protected RecoverPasswordEmailContent newViewModelInstance(final CustomerToken resetPasswordToken) {
        return new RecoverPasswordEmailContent();
    }

    @Override
    public RecoverPasswordEmailContent create(final CustomerToken customerToken) {
        return super.create(customerToken);
    }

    @Override
    protected final void initialize(final RecoverPasswordEmailContent viewModel, final CustomerToken resetPasswordToken) {
        fillPasswordResetUrl(viewModel, resetPasswordToken);
    }

    protected void fillPasswordResetUrl(final RecoverPasswordEmailContent viewModel, final CustomerToken resetPasswordToken) {
        final String passwordResetUrl = createPasswordResetUrl(resetPasswordToken);
        viewModel.setPasswordResetUrl(passwordResetUrl);
    }

    private String createPasswordResetUrl(final CustomerToken resetPasswordToken) {
        final Http.Request request = Http.Context.current().request();
        return recoverPasswordReverseRouter.resetPasswordPageCall(resetPasswordToken.getValue())
                .absoluteURL(request);
    }
}