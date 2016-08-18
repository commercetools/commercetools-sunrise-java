package com.commercetools.sunrise.common.reverserouter;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.orders.Order;
import play.mvc.Call;

import java.util.Locale;
import java.util.Optional;

@ImplementedBy(ReflectionMyOrdersReverseRouter.class)
public interface MyOrdersReverseRouter {

    Call myOrderListPageCall(final String languageTag);

    Call myOrderDetailPageCall(final String languageTag, final String orderNumber);

    default Optional<Call> myOrderDetailPageCall(final Locale locale, final Order order) {
        return Optional.ofNullable(order.getOrderNumber())
                .map(orderNumber -> myOrderDetailPageCall(locale.toLanguageTag(), orderNumber));
    }

    default String myOrderDetailPageUrlOrEmpty(final Locale locale, final Order order) {
        return myOrderDetailPageCall(locale, order).map(Call::url).orElse("");
    }
}
