package com.commercetools.sunrise.myaccount.myorders.myorderdetail;

import com.commercetools.sunrise.shoppingcart.CartLikeBeanFactory;
import io.sphere.sdk.orders.Order;

import javax.inject.Inject;

public class MyOrderDetailPageContentFactory {

    @Inject
    private CartLikeBeanFactory cartLikeBeanFactory;

    public MyOrderDetailPageContent create(final Order order) {
        final MyOrderDetailPageContent content = new MyOrderDetailPageContent();
        fillOrder(content, order);
        return content;
    }

    protected void fillOrder(final MyOrderDetailPageContent content, final Order order) {
        content.setOrder(cartLikeBeanFactory.create(order));
    }
}
