package com.commercetools.sunrise.myaccount.authentication.recoverpassword.recover;

import com.commercetools.sunrise.myaccount.authentication.recoverpassword.reset.ResetPasswordFormData;
import play.data.Form;

/**
 * Creates the page content for resetting the customers password.
 *
 * @see RecoverPasswordPageContent
 * @see ResetPasswordFormData
 */
public class RecoverPasswordPageContentFactory {

    public RecoverPasswordPageContent create(final Form<? extends RecoverPasswordFormData> form) {
        final RecoverPasswordPageContent bean = new RecoverPasswordPageContent();
        initialize(bean, form);
        return bean;
    }

    protected final void initialize(final RecoverPasswordPageContent bean, final Form<? extends RecoverPasswordFormData> form) {
        fillResetPasswordForm(bean, form);
    }

    protected void fillResetPasswordForm(final RecoverPasswordPageContent bean, final Form<? extends RecoverPasswordFormData> form) {
        bean.setPasswordRecoveryForm(form);
    }
}
