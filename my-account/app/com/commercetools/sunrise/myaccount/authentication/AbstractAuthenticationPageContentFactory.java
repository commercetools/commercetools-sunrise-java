package com.commercetools.sunrise.myaccount.authentication;

import com.commercetools.sunrise.framework.viewmodels.content.FormPageContentFactory;
import com.commercetools.sunrise.framework.viewmodels.PageTitleResolver;
import play.data.Form;

public abstract class AbstractAuthenticationPageContentFactory<F> extends FormPageContentFactory<AuthenticationPageContent, Void, F> {

    private final PageTitleResolver pageTitleResolver;

    protected AbstractAuthenticationPageContentFactory(final PageTitleResolver pageTitleResolver) {
        this.pageTitleResolver = pageTitleResolver;
    }

    protected final PageTitleResolver getPageTitleResolver() {
        return pageTitleResolver;
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

    @Override
    protected void fillTitle(final AuthenticationPageContent viewModel, final Void input, final Form<? extends F> form) {
        pageTitleResolver.getOrEmpty("myAccount:authenticationPage.title");
    }

    protected abstract void fillLogInForm(final AuthenticationPageContent viewModel, final Form<? extends F> form);

    protected abstract void fillSignUpForm(final AuthenticationPageContent viewModel, final Form<? extends F> form);

    protected abstract void fillSignUpFormSettings(final AuthenticationPageContent viewModel, final Form<? extends F> form);
}
