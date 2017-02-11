package com.commercetools.sunrise.myaccount.authentication.signup;

import com.commercetools.sunrise.common.pages.AbstractThemeLinksControllerComponent;
import com.commercetools.sunrise.common.pages.PageMeta;
import com.commercetools.sunrise.common.reverserouter.AuthenticationReverseRouter;

import javax.inject.Inject;

final class SignUpThemeLinksControllerComponent extends AbstractThemeLinksControllerComponent {

    private final AuthenticationReverseRouter reverseRouter;

    @Inject
    SignUpThemeLinksControllerComponent(final AuthenticationReverseRouter reverseRouter) {
        this.reverseRouter = reverseRouter;
    }

    @Override
    protected void addThemeLinks(final PageMeta meta) {
        meta.addHalLink(reverseRouter.showLogInForm(), "signUp");
        meta.addHalLink(reverseRouter.processSignUpForm(), "signUpSubmit");
    }
}
