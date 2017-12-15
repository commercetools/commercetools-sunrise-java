package com.commercetools.sunrise.myaccount.myorders.myorderdetail.viewmodels;

import com.commercetools.sunrise.models.carts.CartViewModel;
import com.commercetools.sunrise.core.viewmodels.content.PageContent;

public class MyOrderDetailPageContent extends PageContent {

    private CartViewModel order;

    public MyOrderDetailPageContent() {
    }

    public CartViewModel getOrder() {
        return order;
    }

    public void setOrder(final CartViewModel order) {
        this.order = order;
    }
}
