package com.commercetools.sunrise.myaccount.authentication.signup;

import com.commercetools.sunrise.common.pages.AbstractThemeLinksControllerComponent;
import com.commercetools.sunrise.common.pages.PageMeta;
import com.commercetools.sunrise.common.reverserouter.AuthenticationLocalizedReverseRouter;

import javax.inject.Inject;

final class SignUpThemeLinksControllerComponent extends AbstractThemeLinksControllerComponent {

    private final AuthenticationLocalizedReverseRouter reverseRouter;

    @Inject
    SignUpThemeLinksControllerComponent(final AuthenticationLocalizedReverseRouter reverseRouter) {
        this.reverseRouter = reverseRouter;
    }

    @Override
    protected void addThemeLinks(final PageMeta meta) {
        meta.addHalLink(reverseRouter.showLogInForm(), "signUp");
        meta.addHalLink(reverseRouter.processSignUpForm(), "signUpSubmit");
    }
}
