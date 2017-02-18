package com.commercetools.sunrise.framework.reverserouters.myaccount;

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
        meta.addHalLink(reverseRouter.logInPageCall(), "signIn", "logIn");
        meta.addHalLink(reverseRouter.logInProcessCall(), "logInSubmit");
        meta.addHalLink(reverseRouter.logOutProcessCall(), "logOut");
        meta.addHalLink(reverseRouter.logInPageCall(), "signUp");
        meta.addHalLink(reverseRouter.signUpProcessCall(), "signUpSubmit");
    }
}
