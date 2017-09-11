package com.commercetools.sunrise.myaccount.authentication.recoverpassword.reset;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.reverserouter.RecoverPasswordReverseRouter;
import play.data.Form;

import javax.inject.Inject;

/**
 * Creates the page content for resetting the customers password.
 *
 * @see ResetPasswordPageContent
 * @see ResetPasswordFormData
 */
public class ResetPasswordPageContentFactory {

    @Inject
    private UserContext userContext;
    @Inject
    private RecoverPasswordReverseRouter recoverPasswordReverseRouter;

    protected final UserContext getUserContext() {
        return userContext;
    }

    protected final RecoverPasswordReverseRouter getRecoverPasswordReverseRouter() {
        return recoverPasswordReverseRouter;
    }

    public final ResetPasswordPageContent create( final String resetToken, final Form<? extends ResetPasswordFormData> form) {
        final ResetPasswordPageContent bean = new ResetPasswordPageContent();
        initialize(bean, resetToken, form);
        return bean;
    }

    protected final void initialize(final ResetPasswordPageContent viewModel, final String resetToken, final Form<? extends ResetPasswordFormData> form) {
        fillResetPasswordForm(viewModel, resetToken, form);
        fillResetPasswordUrl(viewModel, resetToken, form);
    }

    protected void fillResetPasswordUrl(final ResetPasswordPageContent viewModel, final String resetToken, final Form<? extends ResetPasswordFormData> form) {
        final String resetPasswordUrl = recoverPasswordReverseRouter.processResetPasswordForm(userContext.languageTag(), resetToken).url();
        viewModel.setResetPasswordUrl(resetPasswordUrl);
    }

    protected void fillResetPasswordForm(final ResetPasswordPageContent viewModel, final String resetToken, final Form<? extends ResetPasswordFormData> form) {
        viewModel.setResetPasswordForm(form);
    }
}
