package com.commercetools.sunrise.myaccount.authentication.recoverpassword.recover.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.content.PageContent;
import com.commercetools.sunrise.myaccount.authentication.recoverpassword.reset.ResetPasswordFormData;
import play.data.Form;

/**
 * The rest password page contains a {@link ResetPasswordFormData}.
 */
public class RecoverPasswordPageContent extends PageContent {
    private Form<?> passwordRecoveryForm;

    public Form<?> getPasswordRecoveryForm() {
        return passwordRecoveryForm;
    }

    public void setPasswordRecoveryForm(final Form<?> passwordRecoveryForm) {
        this.passwordRecoveryForm = passwordRecoveryForm;
    }
}
