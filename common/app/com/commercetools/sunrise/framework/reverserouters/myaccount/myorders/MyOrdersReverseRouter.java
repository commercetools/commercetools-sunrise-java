package com.commercetools.sunrise.framework.reverserouters.myaccount.myorders;

import com.commercetools.sunrise.framework.reverserouters.LocalizedReverseRouter;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.orders.Order;
import play.mvc.Call;

import java.util.Optional;

@ImplementedBy(DefaultMyOrdersReverseRouter.class)
public interface MyOrdersReverseRouter extends SimpleMyOrdersReverseRouter, LocalizedReverseRouter {

    default Call myOrderListPageCall() {
        return myOrderListPageCall(locale().toLanguageTag());
    }

    default Call myOrderDetailPageCall(final String orderIdentifier) {
        return myOrderDetailPageCall(locale().toLanguageTag(), orderIdentifier);
    }

    /**
     * Finds the call to access the order detail page of the given order.
     * @param order the order that we want to access via the page call
     * @return the page call to access the detail page of this order
     */
    Optional<Call> myOrderDetailPageCall(final Order order);
}
