package com.commercetools.sunrise.common.reverserouter;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import play.mvc.Call;

import javax.inject.Inject;
import java.util.Locale;

@RequestScoped
final class ReflectionAuthenticationLocalizedReverseRouter extends AbstractLocalizedReverseRouter implements AuthenticationLocalizedReverseRouter {

    private final AuthenticationReverseRouter delegate;

    @Inject
    private ReflectionAuthenticationLocalizedReverseRouter(final Locale locale, final AuthenticationReverseRouter reverseRouter) {
        super(locale);
        this.delegate = reverseRouter;
    }

    @Override
    public Call showLogInForm(final String languageTag) {
        return delegate.showLogInForm(languageTag);
    }

    @Override
    public Call processLogInForm(final String languageTag) {
        return delegate.processLogInForm(languageTag);
    }

    @Override
    public Call processSignUpForm(final String languageTag) {
        return delegate.processSignUpForm(languageTag);
    }

    @Override
    public Call processLogOut(final String languageTag) {
        return delegate.processLogOut(languageTag);
    }
}
