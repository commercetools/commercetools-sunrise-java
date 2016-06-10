package com.commercetools.sunrise.myaccount.myorders;

import common.controllers.PageContent;
import shoppingcart.CartLikeBean;

public class MyOrderPageContent extends PageContent {

    private CartLikeBean order;

    public MyOrderPageContent() {
    }

    public CartLikeBean getOrder() {
        return order;
    }

    public void setOrder(final CartLikeBean order) {
        this.order = order;
    }
}
