package com.commercetools.sunrise.myaccount.myorders.myorderdetail;

import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.shoppingcart.CartBean;

public class MyOrderDetailPageContent extends PageContent {

    private CartBean order;

    public MyOrderDetailPageContent() {
    }

    public CartBean getOrder() {
        return order;
    }

    public void setOrder(final CartBean order) {
        this.order = order;
    }
}
