package com.commercetools.sunrise.framework.reverserouters.myaccount;

import com.commercetools.sunrise.framework.reverserouters.LocalizedReverseRouter;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.orders.Order;
import play.mvc.Call;

import java.util.Optional;

@ImplementedBy(ReflectionMyOrdersLocalizedReverseRouter.class)
public interface MyOrdersReverseRouter extends MyOrdersSimpleReverseRouter, LocalizedReverseRouter {

    default Call myOrderListPageCall() {
        return myOrderListPageCall(languageTag());
    }

    default Call myOrderDetailPageCall(final String orderNumber) {
        return myOrderDetailPageCall(languageTag(), orderNumber);
    }

    default Optional<Call> myOrderDetailPageCall(final Order order) {
        return myOrderDetailPageCall(locale(), order);
    }

    default String myOrderDetailPageUrlOrEmpty(final Order order) {
        return myOrderDetailPageUrlOrEmpty(locale(), order);
    }
}
