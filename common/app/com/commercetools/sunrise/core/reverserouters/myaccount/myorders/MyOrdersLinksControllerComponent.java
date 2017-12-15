package com.commercetools.sunrise.core.reverserouters.myaccount.myorders;

import com.commercetools.sunrise.core.reverserouters.AbstractLinksControllerComponent;
import com.commercetools.sunrise.core.viewmodels.meta.PageMeta;

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
