package com.commercetools.sunrise.myaccount.authentication.resetpassword.recover.viewmodels;

import com.commercetools.sunrise.core.viewmodels.content.FormPageContentFactory;
import com.commercetools.sunrise.myaccount.authentication.resetpassword.ResetPasswordRequestFormData;
import com.commercetools.sunrise.myaccount.authentication.resetpassword.ResetPasswordFormData;
import play.data.Form;

import javax.inject.Inject;

/**
 * Creates the page content for resetting the customers password.
 *
 * @see RecoverPasswordPageContent
 * @see ResetPasswordFormData
 */
public class RecoverPasswordPageContentFactory extends FormPageContentFactory<RecoverPasswordPageContent, Void, ResetPasswordRequestFormData> {

    @Inject
    protected RecoverPasswordPageContentFactory() {
    }

    @Override
    public final RecoverPasswordPageContent create(final Void input, final Form<? extends ResetPasswordRequestFormData> form) {
        return super.create(input, form);
    }

    public final RecoverPasswordPageContent create(final Form<? extends ResetPasswordRequestFormData> form) {
        return super.create(null, form);
    }

    @Override
    protected RecoverPasswordPageContent newViewModelInstance(final Void input, final Form<? extends ResetPasswordRequestFormData> form) {
        return new RecoverPasswordPageContent();
    }

    @Override
    protected final void initialize(final RecoverPasswordPageContent viewModel, final Void input, final Form<? extends ResetPasswordRequestFormData> form) {
        super.initialize(viewModel, input, form);
        fillResetPasswordForm(viewModel, input, form);
    }

    protected void fillResetPasswordForm(final RecoverPasswordPageContent viewModel, final Void input, final Form<? extends ResetPasswordRequestFormData> form) {
        viewModel.setPasswordRecoveryForm(form);
    }
}
