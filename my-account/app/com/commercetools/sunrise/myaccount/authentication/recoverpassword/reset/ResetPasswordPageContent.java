package com.commercetools.sunrise.myaccount.authentication.recoverpassword.reset;

import com.commercetools.sunrise.common.pages.PageContent;
import play.data.Form;

/**
 * The rest password page contains a {@link ResetPasswordFormData}.
 */
public class ResetPasswordPageContent extends PageContent {
    private Form<?> resetPasswordForm;

    private String resetPasswordUrl;

    public Form<?> getResetPasswordForm() {
        return resetPasswordForm;
    }

    public void setResetPasswordForm(final Form<?> resetPasswordForm) {
        this.resetPasswordForm = resetPasswordForm;
    }

    public String getResetPasswordUrl() {
        return resetPasswordUrl;
    }

    public void setResetPasswordUrl(final String resetPasswordUrl) {
        this.resetPasswordUrl = resetPasswordUrl;
    }
}
