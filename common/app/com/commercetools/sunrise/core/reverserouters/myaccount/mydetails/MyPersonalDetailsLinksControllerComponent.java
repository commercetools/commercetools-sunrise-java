package com.commercetools.sunrise.core.reverserouters.myaccount.mydetails;

import com.commercetools.sunrise.core.reverserouters.AbstractLinksControllerComponent;
import com.commercetools.sunrise.core.viewmodels.meta.PageMeta;

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
