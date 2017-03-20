package com.commercetools.sunrise.framework.reverserouters.myaccount.authentication;

import com.commercetools.sunrise.framework.reverserouters.LocalizedReverseRouter;
import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(DefaultAuthenticationReverseRouter.class)
public interface AuthenticationReverseRouter extends SimpleAuthenticationReverseRouter, LocalizedReverseRouter {

    default Call logInPageCall() {
        return logInPageCall(locale().toLanguageTag());
    }

    default Call logInProcessCall() {
        return logInProcessCall(locale().toLanguageTag());
    }

    default Call signUpProcessCall() {
        return signUpProcessCall(locale().toLanguageTag());
    }

    default Call logOutProcessCall() {
        return logOutProcessCall(locale().toLanguageTag());
    }
}
