package com.commercetools.sunrise.shoppingcart.checkout.thankyou.viewmodels;

import com.commercetools.sunrise.core.viewmodels.content.PageContent;
import io.sphere.sdk.orders.Order;

public class CheckoutThankYouPageContent extends PageContent {

    private Order order;

    public CheckoutThankYouPageContent() {
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(final Order order) {
        this.order = order;
    }
}
