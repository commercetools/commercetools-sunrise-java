package com.commercetools.sunrise.myaccount.myorders;

import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.shoppingcart.CartLikeBean;

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
