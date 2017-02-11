package com.commercetools.sunrise.myaccount.mydetails;

import com.commercetools.sunrise.common.pages.AbstractThemeLinksControllerComponent;
import com.commercetools.sunrise.common.pages.PageMeta;
import com.commercetools.sunrise.common.reverserouter.MyPersonalDetailsReverseRouter;

import javax.inject.Inject;

final class MyPersonalDetailsThemeLinksControllerComponent extends AbstractThemeLinksControllerComponent {

    private final MyPersonalDetailsReverseRouter reverseRouter;

    @Inject
    MyPersonalDetailsThemeLinksControllerComponent(final MyPersonalDetailsReverseRouter reverseRouter) {
        this.reverseRouter = reverseRouter;
    }

    @Override
    protected void addThemeLinks(final PageMeta meta) {
        meta.addHalLink(reverseRouter.myPersonalDetailsPageCall(), "myPersonalDetails", "myAccount");
        meta.addHalLink(reverseRouter.myPersonalDetailsProcessFormCall(), "editMyPersonalDetails");
    }
}
