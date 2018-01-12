package com.commercetools.sunrise.myaccount.authentication.login.viewmodels;

import com.commercetools.sunrise.myaccount.authentication.signup.viewmodels.AbstractAuthenticationPageContentFactory;
import com.commercetools.sunrise.myaccount.authentication.signup.viewmodels.AuthenticationPageContent;
import com.commercetools.sunrise.myaccount.authentication.LogInFormData;
import com.commercetools.sunrise.myaccount.authentication.signup.viewmodels.SignUpFormSettingsViewModelFactory;
import play.data.Form;

import javax.inject.Inject;

public class LogInPageContentFactory extends AbstractAuthenticationPageContentFactory<LogInFormData> {

    private final SignUpFormSettingsViewModelFactory signUpFormSettingsViewModelFactory;

    @Inject
    public LogInPageContentFactory(final SignUpFormSettingsViewModelFactory signUpFormSettingsViewModelFactory) {
        this.signUpFormSettingsViewModelFactory = signUpFormSettingsViewModelFactory;
    }

    protected final SignUpFormSettingsViewModelFactory getSignUpFormSettingsViewModelFactory() {
        return signUpFormSettingsViewModelFactory;
    }

    @Override
    public final AuthenticationPageContent create(final Void input, final Form<? extends LogInFormData> form) {
        return super.create(input, form);
    }

    public final AuthenticationPageContent create(final Form<? extends LogInFormData> form) {
        return super.create(null, form);
    }

    @Override
    protected void fillLogInForm(final AuthenticationPageContent viewModel, final Form<? extends LogInFormData> form) {
        viewModel.setLogInForm(form);
    }

    @Override
    protected void fillSignUpForm(final AuthenticationPageContent viewModel, final Form<? extends LogInFormData> form) {
        viewModel.setSignUpForm(null);
    }

    @Override
    protected void fillSignUpFormSettings(final AuthenticationPageContent viewModel, final Form<? extends LogInFormData> form) {
        viewModel.setSignUpFormSettings(signUpFormSettingsViewModelFactory.create(null));
    }
}
