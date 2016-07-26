package com.commercetools.sunrise.myaccount.myorders.myorderlist;

import com.commercetools.sunrise.common.pages.DefaultWhateverComponent;
import com.commercetools.sunrise.common.pages.PageMeta;
import com.commercetools.sunrise.common.reverserouter.MyOrdersReverseRouter;

import javax.inject.Inject;

final class SunriseMyOrderListHeroldComponent extends DefaultWhateverComponent {
    @Inject
    private MyOrdersReverseRouter reverseRouter;

    protected void updateMeta(final PageMeta meta) {
        meta.addHalLink(reverseRouter.myOrderListPageCall(languageTag()), "myOrders");
    }
}
