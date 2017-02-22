package com.commercetools.sunrise.framework.reverserouters.myaccount;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.orders.Order;
import play.mvc.Call;

import java.util.Locale;
import java.util.Optional;

@ImplementedBy(ReflectionMyOrdersReverseRouter.class)
interface MyOrdersSimpleReverseRouter {

    String MY_ORDER_LIST_PAGE = "myOrderListPageCall";

    Call myOrderListPageCall(final String languageTag);

    String MY_ORDER_DETAIL_PAGE = "myOrderDetailPageCall";

    Call myOrderDetailPageCall(final String languageTag, final String orderIdentifier);

    default Optional<Call> myOrderDetailPageCallByOrderNumber(final Locale locale, final Order order) {
        return Optional.ofNullable(order.getOrderNumber())
                .map(orderNumber -> myOrderDetailPageCall(locale.toLanguageTag(), orderNumber));
    }
}
