package com.commercetools.sunrise.myaccount.authentication.signup;

import com.commercetools.sunrise.common.controllers.AuthenticationReverseRouter;
import com.commercetools.sunrise.common.pages.HeroldComponentBase;
import com.commercetools.sunrise.common.pages.PageMeta;

import javax.inject.Inject;

final class SunriseSignUpHeroldComponent extends HeroldComponentBase {
    @Inject
    private AuthenticationReverseRouter authenticationReverseRouter;

    protected void updateMeta(final PageMeta meta) {
        meta.addHalLink(authenticationReverseRouter.showLogInForm(languageTag()), "signUp");
        meta.addHalLink(authenticationReverseRouter.processSignUpForm(languageTag()), "signUpSubmit");
    }
}
