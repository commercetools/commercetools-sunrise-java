package com.commercetools.sunrise.common.reverserouter;

import com.google.inject.ImplementedBy;
import play.mvc.Call;

@ImplementedBy(ReflectionMyOrdersLocalizedReverseRouter.class)
public interface MyOrdersLocalizedReverseRouter extends MyOrdersReverseRouter, LocalizedReverseRouter {

    default Call myOrderListPageCall() {
        return myOrderListPageCall(languageTag());
    }

    default Call myOrderDetailPageCall(final String orderNumber) {
        return myOrderDetailPageCall(languageTag(), orderNumber);
    }
}
