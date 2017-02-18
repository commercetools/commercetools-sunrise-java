package com.commercetools.sunrise.framework.reverserouters.myaccount;

import com.commercetools.sunrise.common.pages.AbstractLinksControllerComponent;
import com.commercetools.sunrise.common.pages.PageMeta;

import javax.inject.Inject;

public class MyPersonalDetailsLinksControllerComponent extends AbstractLinksControllerComponent<MyPersonalDetailsReverseRouter> {

    private final MyPersonalDetailsReverseRouter reverseRouter;

    @Inject
    protected MyPersonalDetailsLinksControllerComponent(final MyPersonalDetailsReverseRouter reverseRouter) {
        this.reverseRouter = reverseRouter;
    }

    @Override
    public final MyPersonalDetailsReverseRouter getReverseRouter() {
        return reverseRouter;
    }

    @Override
    protected void addLinksToPage(final PageMeta meta, final MyPersonalDetailsReverseRouter reverseRouter) {
        meta.addHalLink(reverseRouter.myPersonalDetailsPageCall(), "myPersonalDetails", "myAccount");
        meta.addHalLink(reverseRouter.myPersonalDetailsProcessCall(), "editMyPersonalDetails");
    }
}
