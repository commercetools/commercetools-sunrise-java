package com.commercetools.sunrise.myaccount.authentication.recoverpassword.recover.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.content.PageContent;

/**
 * The page content for the password reset email template.
 */
public class RecoverPasswordEmailContent extends PageContent {

    private String passwordResetUrl;

    public String getPasswordResetUrl() {
        return passwordResetUrl;
    }

    public void setPasswordResetUrl(final String passwordResetUrl) {
        this.passwordResetUrl = passwordResetUrl;
    }
}