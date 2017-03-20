package com.commercetools.sunrise.framework.reverserouters.myaccount.myorders;

import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(SimpleMyOrdersReverseRouterByReflection.class)
public interface SimpleMyOrdersReverseRouter {

    String MY_ORDER_LIST_PAGE = "myOrderListPageCall";

    Call myOrderListPageCall(final String languageTag);

    String MY_ORDER_DETAIL_PAGE = "myOrderDetailPageCall";

    Call myOrderDetailPageCall(final String languageTag, final String orderIdentifier);
}
