package com.commercetools.sunrise.myaccount.authentication;

import com.commercetools.sunrise.common.models.ControllerData;
import com.commercetools.sunrise.myaccount.authentication.login.LogInFormData;
import com.commercetools.sunrise.myaccount.authentication.signup.SignUpFormData;
import io.sphere.sdk.customers.CustomerSignInResult;
import play.data.Form;

import javax.annotation.Nullable;

public class AuthenticationControllerData extends ControllerData {

    @Nullable
    private final Form<? extends SignUpFormData> signUpForm;
    @Nullable
    private final Form<? extends LogInFormData> logInForm;

    public AuthenticationControllerData(@Nullable final Form<? extends SignUpFormData> signUpForm, @Nullable final Form<? extends LogInFormData> logInForm,
                                        @Nullable final CustomerSignInResult customerSignInResult) {
        this.signUpForm = signUpForm;
        this.logInForm = logInForm;
    }

    @Nullable
    public Form<? extends SignUpFormData> getSignUpForm() {
        return signUpForm;
    }

    @Nullable
    public Form<? extends LogInFormData> getLogInForm() {
        return logInForm;
    }
}
