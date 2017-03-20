package com.commercetools.sunrise.framework.reverserouters.myaccount.myorders;

import com.commercetools.sunrise.framework.reverserouters.AbstractLinksControllerComponent;
import com.commercetools.sunrise.framework.viewmodels.meta.PageMeta;

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
