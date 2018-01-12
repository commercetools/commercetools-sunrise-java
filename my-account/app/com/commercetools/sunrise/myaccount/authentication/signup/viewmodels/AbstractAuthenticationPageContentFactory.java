package com.commercetools.sunrise.myaccount.authentication.signup.viewmodels;

import com.commercetools.sunrise.core.viewmodels.content.FormPageContentFactory;
import com.commercetools.sunrise.myaccount.authentication.signup.viewmodels.AuthenticationPageContent;
import play.data.Form;

public abstract class AbstractAuthenticationPageContentFactory<F> extends FormPageContentFactory<AuthenticationPageContent, Void, F> {

    protected AbstractAuthenticationPageContentFactory() {
    }

    @Override
    protected AuthenticationPageContent newViewModelInstance(final Void input, final Form<? extends F> form) {
        return new AuthenticationPageContent();
    }

    @Override
    protected final void initialize(final AuthenticationPageContent viewModel, final Void input, final Form<? extends F> form) {
        super.initialize(viewModel, input, form);
        fillLogInForm(viewModel, form);
        fillSignUpForm(viewModel, form);
        fillSignUpFormSettings(viewModel, form);
    }

    protected abstract void fillLogInForm(final AuthenticationPageContent viewModel, final Form<? extends F> form);

    protected abstract void fillSignUpForm(final AuthenticationPageContent viewModel, final Form<? extends F> form);

    protected abstract void fillSignUpFormSettings(final AuthenticationPageContent viewModel, final Form<? extends F> form);
}
