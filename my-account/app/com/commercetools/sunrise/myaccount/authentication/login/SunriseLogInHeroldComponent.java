package com.commercetools.sunrise.myaccount.authentication.login;

import com.commercetools.sunrise.common.reverserouter.AuthenticationReverseRouter;
import com.commercetools.sunrise.common.pages.HeroldComponentBase;
import com.commercetools.sunrise.common.pages.PageMeta;

import javax.inject.Inject;

final class SunriseLogInHeroldComponent extends HeroldComponentBase {
    @Inject
    private AuthenticationReverseRouter authenticationReverseRouter;

    protected void updateMeta(final PageMeta meta) {
        meta.addHalLink(authenticationReverseRouter.showLogInForm(languageTag()), "signIn", "logIn");
        meta.addHalLink(authenticationReverseRouter.processLogInForm(languageTag()), "logInSubmit");
    }
}
