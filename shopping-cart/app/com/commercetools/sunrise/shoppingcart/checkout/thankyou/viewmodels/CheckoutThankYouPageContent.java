package com.commercetools.sunrise.shoppingcart.checkout.thankyou.viewmodels;

import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.common.models.carts.OrderBean;

public class CheckoutThankYouPageContent extends PageContent {

    private OrderBean order;

    public CheckoutThankYouPageContent() {
    }

    public OrderBean getOrder() {
        return order;
    }

    public void setOrder(final OrderBean order) {
        this.order = order;
    }
}
