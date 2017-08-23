package com.commercetools.sunrise.myaccount.authentication.recoverpassword.recover.viewmodels;

import com.commercetools.sunrise.framework.reverserouters.myaccount.recoverpassword.RecoverPasswordReverseRouter;
import com.commercetools.sunrise.framework.template.i18n.I18nIdentifierResolver;
import com.commercetools.sunrise.framework.viewmodels.SimpleViewModelFactory;
import io.sphere.sdk.customers.CustomerToken;
import play.mvc.Http;

import javax.inject.Inject;

/**
 * Creates the page content to render the password reset email content.
 */
public class RecoverPasswordEmailPageContentFactory extends SimpleViewModelFactory<RecoverPasswordEmailPageContent, CustomerToken> {
    private final I18nIdentifierResolver i18nIdentifierResolver;
    private final RecoverPasswordReverseRouter recoverPasswordReverseRouter;

    @Inject
    protected RecoverPasswordEmailPageContentFactory(final I18nIdentifierResolver i18nIdentifierResolver, final RecoverPasswordReverseRouter recoverPasswordReverseRouter) {
        this.i18nIdentifierResolver = i18nIdentifierResolver;
        this.recoverPasswordReverseRouter = recoverPasswordReverseRouter;
    }

    @Override
    protected RecoverPasswordEmailPageContent newViewModelInstance(final CustomerToken resetPasswordToken) {
        return new RecoverPasswordEmailPageContent();
    }

    @Override
    protected final void initialize(final RecoverPasswordEmailPageContent viewModel, final CustomerToken resetPasswordToken) {
        fillPasswordResetUrl(viewModel, resetPasswordToken);
        fillSubject(viewModel, resetPasswordToken);
    }

    protected void fillSubject(final RecoverPasswordEmailPageContent viewModel, final CustomerToken resetPasswordToken) {
        viewModel.setSubject(i18nIdentifierResolver.resolveOrEmpty("my-account:forgotPassword.email.subject"));
    }

    protected void fillPasswordResetUrl(final RecoverPasswordEmailPageContent viewModel, final CustomerToken resetPasswordToken) {
        final String passwordResetUrl = createPasswordResetUrl(resetPasswordToken);
        viewModel.setPasswordResetUrl(passwordResetUrl);
    }

    protected String createPasswordResetUrl(final CustomerToken resetPasswordToken) {
        final Http.Request request = Http.Context.current().request();
        final String absoluteURL = recoverPasswordReverseRouter.resetPasswordPageCall(resetPasswordToken.getValue())
                .absoluteURL(request);

        return absoluteURL;
    }
}
