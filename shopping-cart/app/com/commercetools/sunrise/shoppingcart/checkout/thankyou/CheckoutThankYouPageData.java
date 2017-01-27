package com.commercetools.sunrise.shoppingcart.checkout.thankyou;

import io.sphere.sdk.models.Base;
import io.sphere.sdk.orders.Order;

public class CheckoutThankYouPageData extends Base {

    public final Order order;

    public CheckoutThankYouPageData(final Order order) {
        this.order = order;
    }
}
