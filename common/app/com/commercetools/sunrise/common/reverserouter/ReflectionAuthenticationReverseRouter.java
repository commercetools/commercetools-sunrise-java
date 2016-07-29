package com.commercetools.sunrise.common.reverserouter;

import com.commercetools.sunrise.common.pages.ParsedRoutes;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
final class ReflectionAuthenticationReverseRouter extends ReflectionReverseRouterBase implements AuthenticationReverseRouter {

    private ReverseCaller showLogInForm;
    private ReverseCaller processLogInForm;
    private ReverseCaller processSignUpForm;
    private ReverseCaller processLogOut;

    @Inject
    private void setRoutes(final ParsedRoutes parsedRoutes) {
        showLogInForm = getCallerForRoute(parsedRoutes, "showLogInForm");
        processLogInForm = getCallerForRoute(parsedRoutes, "processLogInForm");
        processSignUpForm = getCallerForRoute(parsedRoutes, "processSignUpForm");
        processLogOut = getCallerForRoute(parsedRoutes, "processLogOut");
    }

    @Override
    public Call showLogInForm(final String languageTag) {
        return showLogInForm.call(languageTag);
    }

    @Override
    public Call processLogInForm(final String languageTag) {
        return processLogInForm.call(languageTag);
    }

    @Override
    public Call processSignUpForm(final String languageTag) {
        return processSignUpForm.call(languageTag);
    }

    @Override
    public Call processLogOut(final String languageTag) {
        return processLogOut.call(languageTag);
    }
}
