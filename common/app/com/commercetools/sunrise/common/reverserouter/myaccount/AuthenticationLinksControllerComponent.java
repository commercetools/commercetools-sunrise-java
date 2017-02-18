package com.commercetools.sunrise.common.reverserouter.myaccount;

import com.commercetools.sunrise.common.pages.AbstractLinksControllerComponent;
import com.commercetools.sunrise.common.pages.PageMeta;

import javax.inject.Inject;

public class AuthenticationLinksControllerComponent extends AbstractLinksControllerComponent<AuthenticationReverseRouter> {

    private final AuthenticationReverseRouter reverseRouter;

    @Inject
    protected AuthenticationLinksControllerComponent(final AuthenticationReverseRouter reverseRouter) {
        this.reverseRouter = reverseRouter;
    }

    @Override
    public final AuthenticationReverseRouter getReverseRouter() {
        return reverseRouter;
    }

    @Override
    protected void addLinksToPage(final PageMeta meta, final AuthenticationReverseRouter reverseRouter) {
        meta.addHalLink(reverseRouter.showLogInForm(), "signIn", "logIn");
        meta.addHalLink(reverseRouter.processLogInForm(), "logInSubmit");
        meta.addHalLink(reverseRouter.processLogOut(), "logOut");
        meta.addHalLink(reverseRouter.showLogInForm(), "signUp");
        meta.addHalLink(reverseRouter.processSignUpForm(), "signUpSubmit");
    }
}
