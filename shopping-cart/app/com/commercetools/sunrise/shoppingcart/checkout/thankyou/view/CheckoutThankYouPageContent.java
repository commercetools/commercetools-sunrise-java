package com.commercetools.sunrise.shoppingcart.checkout.thankyou.view;

import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.shoppingcart.OrderBean;

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
