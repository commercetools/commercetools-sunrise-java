package com.commercetools.sunrise.myaccount.authentication.logout;

import com.commercetools.sunrise.common.reverserouter.AuthenticationReverseRouter;
import com.commercetools.sunrise.common.pages.HeroldComponentBase;
import com.commercetools.sunrise.common.pages.PageMeta;

import javax.inject.Inject;

final class SunriseLogOutHeroldComponent extends HeroldComponentBase {
    @Inject
    private AuthenticationReverseRouter authenticationReverseRouter;

    protected void updateMeta(final PageMeta meta) {
        meta.addHalLink(authenticationReverseRouter.processLogOut(languageTag()), "logOut");
    }
}
