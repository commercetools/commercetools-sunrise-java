package com.commercetools.sunrise.myaccount.authentication.login;

import com.commercetools.sunrise.common.pages.AbstractThemeLinksControllerComponent;
import com.commercetools.sunrise.common.pages.PageMeta;
import com.commercetools.sunrise.common.reverserouter.AuthenticationReverseRouter;

import javax.inject.Inject;

final class LogInThemeLinksControllerComponent extends AbstractThemeLinksControllerComponent {

    private final AuthenticationReverseRouter reverseRouter;

    @Inject
    LogInThemeLinksControllerComponent(final AuthenticationReverseRouter reverseRouter) {
        this.reverseRouter = reverseRouter;
    }

    @Override
    protected void addThemeLinks(final PageMeta meta) {
        meta.addHalLink(reverseRouter.showLogInForm(), "signIn", "logIn");
        meta.addHalLink(reverseRouter.processLogInForm(), "logInSubmit");
    }
}
