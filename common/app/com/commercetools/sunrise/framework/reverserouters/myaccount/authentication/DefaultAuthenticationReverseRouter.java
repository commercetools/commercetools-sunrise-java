package com.commercetools.sunrise.framework.reverserouters.myaccount.authentication;

import com.commercetools.sunrise.framework.reverserouters.AbstractLocalizedReverseRouter;
import play.mvc.Call;

import javax.inject.Inject;
import java.util.Locale;

public class DefaultAuthenticationReverseRouter extends AbstractLocalizedReverseRouter implements AuthenticationReverseRouter {

    private final SimpleAuthenticationReverseRouter delegate;

    @Inject
    protected DefaultAuthenticationReverseRouter(final Locale locale, final SimpleAuthenticationReverseRouter reverseRouter) {
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
