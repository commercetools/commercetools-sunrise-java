package com.commercetools.sunrise.myaccount.myorders.myorderlist;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.pages.PageData;
import com.commercetools.sunrise.common.reverserouter.MyOrdersReverseRouter;
import com.commercetools.sunrise.framework.ControllerComponent;
import com.commercetools.sunrise.hooks.PageDataHook;
import io.sphere.sdk.models.Base;

import javax.inject.Inject;

final class SunriseMyOrderListComponent extends Base implements ControllerComponent, PageDataHook {
    @Inject
    private MyOrdersReverseRouter reverseRouter;
    @Inject
    private UserContext userContext;

    @Override
    public void acceptPageData(final PageData pageData) {
        pageData.getMeta().addHalLink(reverseRouter.myOrderListPageCall(userContext.languageTag()), "myOrders");
    }
}
