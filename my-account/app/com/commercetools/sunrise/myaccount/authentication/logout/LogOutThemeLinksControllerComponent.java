package com.commercetools.sunrise.myaccount.authentication.logout;

import com.commercetools.sunrise.common.pages.AbstractThemeLinksControllerComponent;
import com.commercetools.sunrise.common.pages.PageMeta;
import com.commercetools.sunrise.common.reverserouter.AuthenticationReverseRouter;

import javax.inject.Inject;

final class LogOutThemeLinksControllerComponent extends AbstractThemeLinksControllerComponent {

    private final AuthenticationReverseRouter reverseRouter;

    @Inject
    LogOutThemeLinksControllerComponent(final AuthenticationReverseRouter reverseRouter) {
        this.reverseRouter = reverseRouter;
    }

    @Override
    protected void addThemeLinks(final PageMeta meta) {
        meta.addHalLink(reverseRouter.processLogOut(), "logOut");
    }
}
