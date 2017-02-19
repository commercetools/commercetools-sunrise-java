package com.commercetools.sunrise.myaccount.authentication.signup.viewmodels;

import com.commercetools.sunrise.common.utils.PageTitleResolver;
import com.commercetools.sunrise.myaccount.authentication.AbstractAuthenticationPageContentFactory;
import com.commercetools.sunrise.myaccount.authentication.AuthenticationPageContent;
import com.commercetools.sunrise.myaccount.authentication.signup.SignUpFormData;
import play.data.Form;

import javax.inject.Inject;

public class SignUpPageContentFactory extends AbstractAuthenticationPageContentFactory<SignUpFormData> {

    private final SignUpFormSettingsBeanFactory signUpFormSettingsBeanFactory;

    @Inject
    public SignUpPageContentFactory(final PageTitleResolver pageTitleResolver, final SignUpFormSettingsBeanFactory signUpFormSettingsBeanFactory) {
        super(pageTitleResolver);
        this.signUpFormSettingsBeanFactory = signUpFormSettingsBeanFactory;
    }

    @Override
    protected void fillLogInForm(final AuthenticationPageContent model, final Form<? extends SignUpFormData> form) {
        model.setLogInForm(null);
    }

    @Override
    protected void fillSignUpForm(final AuthenticationPageContent model, final Form<? extends SignUpFormData> form) {
        model.setSignUpForm(form);
    }

    @Override
    protected void fillSignUpFormSettings(final AuthenticationPageContent model, final Form<? extends SignUpFormData> form) {
        model.setSignUpFormSettings(signUpFormSettingsBeanFactory.create(form));
    }
}
