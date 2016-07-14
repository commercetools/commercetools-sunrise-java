package com.commercetools.sunrise.myaccount.authentication;

import com.commercetools.sunrise.common.forms.TitleFormFieldBeanFactory;
import com.commercetools.sunrise.myaccount.authentication.signup.SignUpFormSettingsBean;
import io.sphere.sdk.models.Base;
import play.data.Form;
import play.data.FormFactory;

import javax.inject.Inject;

public class AuthenticationPageContentFactory extends Base {

    @Inject
    private TitleFormFieldBeanFactory titleFormFieldBeanFactory;
    @Inject
    private FormFactory formFactory;

    public AuthenticationPageContent createWithSignUpForm(final Form<?> signUpForm) {
        return fillBeanWithSignUpForm(new AuthenticationPageContent(), signUpForm);
    }

    protected <T extends AuthenticationPageContent> T fillBeanWithSignUpForm(final T pageContent, final Form<?> signUpForm) {
        fillLogInForm(pageContent, formFactory.form());
        fillSignUpForm(pageContent, signUpForm);
        return pageContent;
    }

    public AuthenticationPageContent createWithLogInForm(final Form<?> logInForm) {
        final AuthenticationPageContent pageContent = new AuthenticationPageContent();
        return fillBeanWithLogInForm(pageContent, logInForm);
    }

    protected <T extends AuthenticationPageContent> T fillBeanWithLogInForm(final T pageContent, final Form<?> logInForm) {
        fillSignUpForm(pageContent, formFactory.form());
        fillLogInForm(pageContent, logInForm);
        return pageContent;
    }

    protected void fillLogInForm(final AuthenticationPageContent content, final Form<?> form) {
        content.setLogInForm(form);
    }

    protected void fillSignUpForm(final AuthenticationPageContent content, final Form<?> form) {
        content.setSignUpFormSettings(createFormSettings(form));
        content.setSignUpForm(form);
    }

    protected SignUpFormSettingsBean createFormSettings(final Form<?> form) {
        final SignUpFormSettingsBean bean = new SignUpFormSettingsBean();
        bean.setTitle(titleFormFieldBeanFactory.createWithDefaultTitles(form, "title"));
        return bean;
    }
}
