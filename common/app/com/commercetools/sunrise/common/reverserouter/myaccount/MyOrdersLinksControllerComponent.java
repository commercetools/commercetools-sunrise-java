package com.commercetools.sunrise.common.reverserouter.myaccount;

import com.commercetools.sunrise.common.pages.AbstractLinksControllerComponent;
import com.commercetools.sunrise.common.pages.PageMeta;

import javax.inject.Inject;

public class MyOrdersLinksControllerComponent extends AbstractLinksControllerComponent<MyOrdersReverseRouter> {

    private final MyOrdersReverseRouter reverseRouter;

    @Inject
    protected MyOrdersLinksControllerComponent(final MyOrdersReverseRouter reverseRouter) {
        this.reverseRouter = reverseRouter;
    }

    @Override
    public final MyOrdersReverseRouter getReverseRouter() {
        return reverseRouter;
    }

    @Override
    protected void addLinksToPage(final PageMeta meta, final MyOrdersReverseRouter reverseRouter) {
        meta.addHalLink(reverseRouter.myOrderListPageCall(), "myOrders");
    }
}
