package com.commercetools.sunrise.framework.reverserouters.myaccount;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.reverserouters.AbstractLocalizedReverseRouter;
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
    public Call logInPageCall(final String languageTag) {
        return delegate.logInPageCall(languageTag);
    }

    @Override
    public Call logInProcessCall(final String languageTag) {
        return delegate.logInProcessCall(languageTag);
    }

    @Override
    public Call signUpProcessCall(final String languageTag) {
        return delegate.signUpProcessCall(languageTag);
    }

    @Override
    public Call logOutProcessCall(final String languageTag) {
        return delegate.logOutProcessCall(languageTag);
    }
}
