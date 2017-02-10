package com.commercetools.sunrise.myaccount.authentication.logout;

import com.commercetools.sunrise.common.pages.AbstractThemeLinksControllerComponent;
import com.commercetools.sunrise.common.pages.PageMeta;
import com.commercetools.sunrise.common.reverserouter.AuthenticationLocalizedReverseRouter;

import javax.inject.Inject;

final class LogOutThemeLinksControllerComponent extends AbstractThemeLinksControllerComponent {

    private final AuthenticationLocalizedReverseRouter reverseRouter;

    @Inject
    LogOutThemeLinksControllerComponent(final AuthenticationLocalizedReverseRouter reverseRouter) {
        this.reverseRouter = reverseRouter;
    }

    @Override
    protected void addThemeLinks(final PageMeta meta) {
        meta.addHalLink(reverseRouter.processLogOut(), "logOut");
    }
}
