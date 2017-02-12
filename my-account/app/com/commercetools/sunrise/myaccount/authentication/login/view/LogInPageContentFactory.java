package com.commercetools.sunrise.myaccount.authentication.login.view;

import com.commercetools.sunrise.common.utils.PageTitleResolver;
import com.commercetools.sunrise.myaccount.authentication.AbstractAuthenticationPageContentFactory;
import com.commercetools.sunrise.myaccount.authentication.AuthenticationPageContent;
import com.commercetools.sunrise.myaccount.authentication.login.LogInFormData;
import com.commercetools.sunrise.myaccount.authentication.signup.view.SignUpFormSettingsBeanFactory;
import play.data.Form;

import javax.inject.Inject;

public class LogInPageContentFactory extends AbstractAuthenticationPageContentFactory<LogInFormData> {

    private final SignUpFormSettingsBeanFactory signUpFormSettingsBeanFactory;

    @Inject
    public LogInPageContentFactory(final PageTitleResolver pageTitleResolver, final SignUpFormSettingsBeanFactory signUpFormSettingsBeanFactory) {
        super(pageTitleResolver);
        this.signUpFormSettingsBeanFactory = signUpFormSettingsBeanFactory;
    }

    @Override
    protected void fillLogInForm(final AuthenticationPageContent model, final Form<? extends LogInFormData> form) {
        model.setLogInForm(form);
    }

    @Override
    protected void fillSignUpForm(final AuthenticationPageContent model, final Form<? extends LogInFormData> form) {
        model.setSignUpForm(null);
    }

    @Override
    protected void fillSignUpFormSettings(final AuthenticationPageContent model, final Form<? extends LogInFormData> form) {
        model.setSignUpFormSettings(signUpFormSettingsBeanFactory.create(null));
    }
}
