package com.commercetools.sunrise.myaccount.authentication.recoverpassword.reset.viewmodels;

import com.commercetools.sunrise.framework.reverserouters.myaccount.recoverpassword.RecoverPasswordReverseRouter;
import com.commercetools.sunrise.framework.viewmodels.PageTitleResolver;
import com.commercetools.sunrise.framework.viewmodels.content.FormPageContentFactory;
import com.commercetools.sunrise.myaccount.authentication.recoverpassword.reset.ResetPasswordFormData;
import play.data.Form;

import javax.inject.Inject;

/**
 * Creates the page content for resetting the customers password.
 *
 * @see ResetPasswordPageContent
 * @see ResetPasswordFormData
 */
public class ResetPasswordPageContentFactory extends FormPageContentFactory<ResetPasswordPageContent, String, ResetPasswordFormData> {
    private final PageTitleResolver pageTitleResolver;
    private final RecoverPasswordReverseRouter recoverPasswordReverseRouter;

    @Inject
    protected ResetPasswordPageContentFactory(final PageTitleResolver pageTitleResolver, final RecoverPasswordReverseRouter recoverPasswordReverseRouter) {
        this.pageTitleResolver = pageTitleResolver;
        this.recoverPasswordReverseRouter = recoverPasswordReverseRouter;
    }

    protected final PageTitleResolver getPageTitleResolver() {
        return pageTitleResolver;
    }

    @Override
    protected ResetPasswordPageContent newViewModelInstance(final String resetToken, final Form<? extends ResetPasswordFormData> form) {
        return new ResetPasswordPageContent();
    }

    @Override
    protected void fillTitle(final ResetPasswordPageContent viewModel, final String resetToken, final Form<? extends ResetPasswordFormData> form) {
        viewModel.setTitle(pageTitleResolver.getOrEmpty("my-account:resetPassword.title"));
    }

    public final ResetPasswordPageContent create( final String resetToken, final Form<? extends ResetPasswordFormData> form) {
        return super.create(resetToken, form);
    }

    @Override
    protected final void initialize(final ResetPasswordPageContent viewModel, final String resetToken, final Form<? extends ResetPasswordFormData> form) {
        super.initialize(viewModel, resetToken, form);
        fillResetPasswordForm(viewModel, resetToken, form);
        fillResetPasswordUrl(viewModel, resetToken, form);
    }

    protected void fillResetPasswordUrl(final ResetPasswordPageContent viewModel, final String resetToken, final Form<? extends ResetPasswordFormData> form) {
        final String resetPasswordUrl = recoverPasswordReverseRouter.resetPasswordProcessCall(resetToken).url();
        viewModel.setResetPasswordUrl(resetPasswordUrl);
    }

    protected void fillResetPasswordForm(final ResetPasswordPageContent viewModel, final String resetToken, final Form<? extends ResetPasswordFormData> form) {
        viewModel.setResetPasswordForm(form);
    }
}
