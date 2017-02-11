package com.commercetools.sunrise.myaccount.authentication;

import com.commercetools.sunrise.common.models.FormPageContentFactory;
import com.commercetools.sunrise.common.utils.PageTitleResolver;
import io.sphere.sdk.customers.CustomerSignInResult;
import play.data.Form;

import javax.annotation.Nullable;

public abstract class AbstractAuthenticationPageContentFactory<F> extends FormPageContentFactory<AuthenticationPageContent, CustomerSignInResult, F> {

    private final PageTitleResolver pageTitleResolver;

    protected AbstractAuthenticationPageContentFactory(final PageTitleResolver pageTitleResolver) {
        this.pageTitleResolver = pageTitleResolver;
    }

    @Override
    protected AuthenticationPageContent getViewModelInstance() {
        return new AuthenticationPageContent();
    }

    @Override
    protected final void initialize(final AuthenticationPageContent model, @Nullable final CustomerSignInResult result, final Form<? extends F> form) {
        super.initialize(model, result, form);
        fillLogInForm(model, result, form);
        fillSignUpForm(model, result, form);
        fillSignUpFormSettings(model, result, form);
    }

    @Override
    protected void fillTitle(final AuthenticationPageContent model, @Nullable final CustomerSignInResult result, final Form<? extends F> form) {
        pageTitleResolver.getOrEmpty("myAccount:authenticationPage.title");
    }

    protected abstract void fillLogInForm(final AuthenticationPageContent model, @Nullable final CustomerSignInResult result, final Form<? extends F> form);

    protected abstract void fillSignUpForm(final AuthenticationPageContent model, @Nullable final CustomerSignInResult result, final Form<? extends F> form);

    protected abstract void fillSignUpFormSettings(final AuthenticationPageContent model, @Nullable final CustomerSignInResult result, final Form<? extends F> form);
}
