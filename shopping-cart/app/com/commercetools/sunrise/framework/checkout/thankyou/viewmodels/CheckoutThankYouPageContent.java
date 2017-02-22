package com.commercetools.sunrise.framework.checkout.thankyou.viewmodels;

import com.commercetools.sunrise.common.models.carts.OrderViewModel;
import com.commercetools.sunrise.common.pages.PageContent;

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
