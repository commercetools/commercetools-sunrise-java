package com.commercetools.sunrise.myaccount.authentication;

import com.commercetools.sunrise.common.models.PageContentFactory;
import com.commercetools.sunrise.common.utils.PageTitleResolver;
import play.data.Form;

public abstract class AbstractAuthenticationPageContentFactory<F> extends PageContentFactory<AuthenticationPageContent, Form<? extends F>> {

    private final PageTitleResolver pageTitleResolver;

    protected AbstractAuthenticationPageContentFactory(final PageTitleResolver pageTitleResolver) {
        this.pageTitleResolver = pageTitleResolver;
    }

    @Override
    protected AuthenticationPageContent getViewModelInstance() {
        return new AuthenticationPageContent();
    }

    @Override
    protected final void initialize(final AuthenticationPageContent model, final Form<? extends F> form) {
        super.initialize(model, form);
        fillLogInForm(model, form);
        fillSignUpForm(model, form);
        fillSignUpFormSettings(model, form);
    }

    @Override
    protected void fillTitle(final AuthenticationPageContent model, final Form<? extends F> form) {
        pageTitleResolver.getOrEmpty("myAccount:authenticationPage.title");
    }

    protected abstract void fillLogInForm(final AuthenticationPageContent model, final Form<? extends F> form);

    protected abstract void fillSignUpForm(final AuthenticationPageContent model, final Form<? extends F> form);

    protected abstract void fillSignUpFormSettings(final AuthenticationPageContent model, final Form<? extends F> form);
}
