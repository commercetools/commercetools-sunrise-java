package com.commercetools.sunrise.shoppingcart.checkout.thankyou.viewmodels;

import com.commercetools.sunrise.models.carts.OrderViewModel;
import com.commercetools.sunrise.core.viewmodels.content.PageContent;

public class CheckoutThankYouPageContent extends PageContent {

    private OrderViewModel order;

    public CheckoutThankYouPageContent() {
    }

    public OrderViewModel getOrder() {
        return order;
    }

    public void setOrder(final OrderViewModel order) {
        this.order = order;
    }
}
