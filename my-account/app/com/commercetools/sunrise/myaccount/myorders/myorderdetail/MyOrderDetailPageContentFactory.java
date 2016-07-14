package com.commercetools.sunrise.myaccount.myorders.myorderdetail;

import com.commercetools.sunrise.shoppingcart.CartLikeBeanFactory;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.orders.Order;

import javax.inject.Inject;

public class MyOrderDetailPageContentFactory extends Base {

    @Inject
    private CartLikeBeanFactory cartLikeBeanFactory;

    public MyOrderDetailPageContent create(final Order order) {
        return fillBean(new MyOrderDetailPageContent(), order);
    }

    protected <T extends MyOrderDetailPageContent> T fillBean(final T bean, final Order order) {
        fillOrder(bean, order);
        return bean;
    }

    protected void fillOrder(final MyOrderDetailPageContent content, final Order order) {
        content.setOrder(cartLikeBeanFactory.create(order));
    }
}
