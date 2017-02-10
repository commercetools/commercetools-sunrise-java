package com.commercetools.sunrise.myaccount.authentication.login;

import com.commercetools.sunrise.common.pages.AbstractThemeLinksControllerComponent;
import com.commercetools.sunrise.common.pages.PageMeta;
import com.commercetools.sunrise.common.reverserouter.AuthenticationLocalizedReverseRouter;

import javax.inject.Inject;

final class LogInThemeLinksControllerComponent extends AbstractThemeLinksControllerComponent {

    private final AuthenticationLocalizedReverseRouter reverseRouter;

    @Inject
    LogInThemeLinksControllerComponent(final AuthenticationLocalizedReverseRouter reverseRouter) {
        this.reverseRouter = reverseRouter;
    }

    @Override
    protected void addThemeLinks(final PageMeta meta) {
        meta.addHalLink(reverseRouter.showLogInForm(), "signIn", "logIn");
        meta.addHalLink(reverseRouter.processLogInForm(), "logInSubmit");
    }
}
