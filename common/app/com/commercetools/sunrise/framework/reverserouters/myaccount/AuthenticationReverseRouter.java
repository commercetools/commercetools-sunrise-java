package com.commercetools.sunrise.framework.reverserouters.myaccount;

import com.commercetools.sunrise.framework.reverserouters.LocalizedReverseRouter;
import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(ReflectionAuthenticationLocalizedReverseRouter.class)
public interface AuthenticationReverseRouter extends AuthenticationSimpleReverseRouter, LocalizedReverseRouter {

    default Call logInPageCall() {
        return logInPageCall(languageTag());
    }

    default Call logInProcessCall() {
        return logInProcessCall(languageTag());
    }

    default Call signUpProcessCall() {
        return signUpProcessCall(languageTag());
    }

    default Call logOutProcessCall() {
        return logOutProcessCall(languageTag());
    }
}
