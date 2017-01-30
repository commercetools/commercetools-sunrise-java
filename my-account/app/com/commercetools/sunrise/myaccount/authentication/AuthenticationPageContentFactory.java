package com.commercetools.sunrise.myaccount.authentication;

import com.commercetools.sunrise.common.models.PageContentFactory;
import com.commercetools.sunrise.common.utils.PageTitleResolver;
import com.commercetools.sunrise.myaccount.authentication.signup.SignUpFormSettingsBeanFactory;

import javax.inject.Inject;

public class AuthenticationPageContentFactory extends PageContentFactory<AuthenticationPageContent, AuthenticationControllerData> {

    private final PageTitleResolver pageTitleResolver;
    private final SignUpFormSettingsBeanFactory signUpFormSettingsBeanFactory;

    @Inject
    public AuthenticationPageContentFactory(final PageTitleResolver pageTitleResolver, final SignUpFormSettingsBeanFactory signUpFormSettingsBeanFactory) {
        this.pageTitleResolver = pageTitleResolver;
        this.signUpFormSettingsBeanFactory = signUpFormSettingsBeanFactory;
    }

    @Override
    protected AuthenticationPageContent getViewModelInstance() {
        return new AuthenticationPageContent();
    }

    @Override
    public final AuthenticationPageContent create(final AuthenticationControllerData data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final AuthenticationPageContent model, final AuthenticationControllerData data) {
        super.initialize(model, data);
        fillLogInForm(model, data);
        fillSignUpForm(model, data);
        fillSignUpFormSettings(model, data);
    }

    @Override
    protected void fillTitle(final AuthenticationPageContent model, final AuthenticationControllerData data) {
        pageTitleResolver.getOrEmpty("myAccount:authenticationPage.title");
    }

    protected void fillLogInForm(final AuthenticationPageContent model, final AuthenticationControllerData data) {
        model.setLogInForm(data.getLogInForm());
    }

    protected void fillSignUpForm(final AuthenticationPageContent model, final AuthenticationControllerData data) {
        model.setSignUpForm(data.getSignUpForm());
    }

    protected void fillSignUpFormSettings(final AuthenticationPageContent model, final AuthenticationControllerData data) {
        model.setSignUpFormSettings(signUpFormSettingsBeanFactory.create(data));
    }
}
