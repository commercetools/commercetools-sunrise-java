package com.commercetools.sunrise.common.reverserouter.myaccount;

import com.commercetools.sunrise.common.injection.RequestScoped;
import com.commercetools.sunrise.common.reverserouter.AbstractLocalizedReverseRouter;
import play.mvc.Call;

import javax.inject.Inject;
import java.util.Locale;

@RequestScoped
final class ReflectionAuthenticationLocalizedReverseRouter extends AbstractLocalizedReverseRouter implements AuthenticationReverseRouter {

    private final AuthenticationSimpleReverseRouter delegate;

    @Inject
    private ReflectionAuthenticationLocalizedReverseRouter(final Locale locale, final AuthenticationSimpleReverseRouter reverseRouter) {
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
