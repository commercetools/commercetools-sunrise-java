package com.commercetools.sunrise.myaccount.authentication;

import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.myaccount.authentication.signup.SignUpFormSettingsBean;
import play.data.Form;

public class AuthenticationPageContent extends PageContent {

    private Form<?> logInForm;
    private Form<?> signUpForm;
    private SignUpFormSettingsBean signUpFormSettings;

    public Form<?> getLogInForm() {
        return logInForm;
    }

    public void setLogInForm(final Form<?> logInForm) {
        this.logInForm = logInForm;
    }

    public Form<?> getSignUpForm() {
        return signUpForm;
    }

    public void setSignUpForm(final Form<?> signUpForm) {
        this.signUpForm = signUpForm;
    }

    public SignUpFormSettingsBean getSignUpFormSettings() {
        return signUpFormSettings;
    }

    public void setSignUpFormSettings(final SignUpFormSettingsBean signUpFormSettings) {
        this.signUpFormSettings = signUpFormSettings;
    }
}
