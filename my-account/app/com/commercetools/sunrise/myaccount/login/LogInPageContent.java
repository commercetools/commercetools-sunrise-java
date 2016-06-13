package com.commercetools.sunrise.myaccount.login;

import com.commercetools.sunrise.common.pages.PageContent;

public class LogInPageContent extends PageContent {

    private LogInFormBean logInForm;
    private SignUpFormBean signUpForm;

    public LogInPageContent() {
    }

    public LogInFormBean getLogInForm() {
        return logInForm;
    }

    public void setLogInForm(final LogInFormBean logInForm) {
        this.logInForm = logInForm;
    }

    public SignUpFormBean getSignUpForm() {
        return signUpForm;
    }

    public void setSignUpForm(final SignUpFormBean signUpForm) {
        this.signUpForm = signUpForm;
    }
}
