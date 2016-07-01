package com.commercetools.sunrise.myaccount.myorders.myorderdetail;

import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.shoppingcart.CartLikeBean;

public class MyOrderDetailPageContent extends PageContent {

    private CartLikeBean order;

    public MyOrderDetailPageContent() {
    }

    public CartLikeBean getOrder() {
        return order;
    }

    public void setOrder(final CartLikeBean order) {
        this.order = order;
    }
}
