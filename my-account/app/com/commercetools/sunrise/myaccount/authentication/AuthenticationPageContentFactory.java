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
        final AuthenticationPageContent bean = new AuthenticationPageContent();
        initializeWithSignUpForm(bean, signUpForm);
        return bean;
    }

    public AuthenticationPageContent createWithLogInForm(final Form<?> logInForm) {
        final AuthenticationPageContent bean = new AuthenticationPageContent();
        initializeWithLogInForm(bean, logInForm);
        return bean;
    }

    protected final void initializeWithSignUpForm(final AuthenticationPageContent bean, final Form<?> signUpForm) {
        fillLogInForm(bean, formFactory.form());
        fillSignUpForm(bean, signUpForm);
    }

    protected void initializeWithLogInForm(final AuthenticationPageContent pageContent, final Form<?> logInForm) {
        fillSignUpForm(pageContent, formFactory.form());
        fillLogInForm(pageContent, logInForm);
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
