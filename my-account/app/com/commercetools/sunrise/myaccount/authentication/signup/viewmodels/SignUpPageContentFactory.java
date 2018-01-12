package com.commercetools.sunrise.myaccount.authentication.signup.viewmodels;

import com.commercetools.sunrise.myaccount.authentication.SignUpFormData;
import play.data.Form;

import javax.inject.Inject;

public class SignUpPageContentFactory extends AbstractAuthenticationPageContentFactory<SignUpFormData> {

    private final SignUpFormSettingsViewModelFactory signUpFormSettingsViewModelFactory;

    @Inject
    public SignUpPageContentFactory(final SignUpFormSettingsViewModelFactory signUpFormSettingsViewModelFactory) {
        this.signUpFormSettingsViewModelFactory = signUpFormSettingsViewModelFactory;
    }

    protected final SignUpFormSettingsViewModelFactory getSignUpFormSettingsViewModelFactory() {
        return signUpFormSettingsViewModelFactory;
    }

    @Override
    public final AuthenticationPageContent create(final Void input, final Form<? extends SignUpFormData> form) {
        return super.create(input, form);
    }

    public final AuthenticationPageContent create(final Form<? extends SignUpFormData> form) {
        return super.create(null, form);
    }

    @Override
    protected void fillLogInForm(final AuthenticationPageContent viewModel, final Form<? extends SignUpFormData> form) {
        viewModel.setLogInForm(null);
    }

    @Override
    protected void fillSignUpForm(final AuthenticationPageContent viewModel, final Form<? extends SignUpFormData> form) {
        viewModel.setSignUpForm(form);
    }

    @Override
    protected void fillSignUpFormSettings(final AuthenticationPageContent viewModel, final Form<? extends SignUpFormData> form) {
        viewModel.setSignUpFormSettings(signUpFormSettingsViewModelFactory.create(form));
    }
}
