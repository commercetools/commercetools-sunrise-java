package com.commercetools.sunrise.shoppingcart.checkout.thankyou;

import com.commercetools.sunrise.common.models.ControllerData;
import io.sphere.sdk.orders.Order;

public class CheckoutThankYouPageControllerData extends ControllerData {

    private final Order order;

    public CheckoutThankYouPageControllerData(final Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}
