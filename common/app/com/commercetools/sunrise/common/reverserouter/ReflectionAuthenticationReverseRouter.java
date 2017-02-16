package com.commercetools.sunrise.common.reverserouter;

import com.commercetools.sunrise.framework.ParsedRouteList;
import play.mvc.Call;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
final class ReflectionAuthenticationReverseRouter extends AbstractReflectionReverseRouter implements AuthenticationSimpleReverseRouter {

    private final ReverseCaller showLogInForm;
    private final ReverseCaller processLogInForm;
    private final ReverseCaller processSignUpForm;
    private final ReverseCaller processLogOut;

    @Inject
    private ReflectionAuthenticationReverseRouter(final ParsedRouteList parsedRouteList) {
        showLogInForm = getCallerForRoute(parsedRouteList, "showLogInForm");
        processLogInForm = getCallerForRoute(parsedRouteList, "processLogInForm");
        processSignUpForm = getCallerForRoute(parsedRouteList, "processSignUpForm");
        processLogOut = getCallerForRoute(parsedRouteList, "processLogOut");
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
