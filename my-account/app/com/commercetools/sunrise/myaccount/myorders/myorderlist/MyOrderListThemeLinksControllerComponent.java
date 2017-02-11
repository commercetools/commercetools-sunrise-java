package com.commercetools.sunrise.myaccount.myorders.myorderlist;

import com.commercetools.sunrise.common.pages.AbstractThemeLinksControllerComponent;
import com.commercetools.sunrise.common.pages.PageMeta;
import com.commercetools.sunrise.common.reverserouter.MyOrdersReverseRouter;

import javax.inject.Inject;

final class MyOrderListThemeLinksControllerComponent extends AbstractThemeLinksControllerComponent {

    private final MyOrdersReverseRouter reverseRouter;

    @Inject
    MyOrderListThemeLinksControllerComponent(final MyOrdersReverseRouter reverseRouter) {
        this.reverseRouter = reverseRouter;
    }

    @Override
    protected void addThemeLinks(final PageMeta meta) {
        meta.addHalLink(reverseRouter.myOrderListPageCall(), "myOrders");
    }
}
