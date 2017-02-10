package com.commercetools.sunrise.common.reverserouter;

import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(ReflectionAuthenticationLocalizedReverseRouter.class)
public interface AuthenticationLocalizedReverseRouter extends AuthenticationReverseRouter, LocalizedReverseRouter {

    default Call showLogInForm() {
        return showLogInForm(languageTag());
    }

    default Call processLogInForm() {
        return processLogInForm(languageTag());
    }

    default Call processSignUpForm() {
        return processSignUpForm(languageTag());
    }

    default Call processLogOut() {
        return processLogOut(languageTag());
    }
}
