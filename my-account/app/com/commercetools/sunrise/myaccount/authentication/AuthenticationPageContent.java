package com.commercetools.sunrise.myaccount.authentication;

import com.commercetools.sunrise.framework.viewmodels.content.PageContent;
import com.commercetools.sunrise.myaccount.authentication.signup.viewmodels.SignUpFormSettingsViewModel;
import play.data.Form;

public class AuthenticationPageContent extends PageContent {

    private Form<?> logInForm;
    private Form<?> signUpForm;
    private SignUpFormSettingsViewModel signUpFormSettings;

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

    public SignUpFormSettingsViewModel getSignUpFormSettings() {
        return signUpFormSettings;
    }

    public void setSignUpFormSettings(final SignUpFormSettingsViewModel signUpFormSettings) {
        this.signUpFormSettings = signUpFormSettings;
    }
}
