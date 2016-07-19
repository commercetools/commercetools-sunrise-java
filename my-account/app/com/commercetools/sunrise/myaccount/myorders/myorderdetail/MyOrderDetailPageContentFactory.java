package com.commercetools.sunrise.myaccount.myorders.myorderdetail;

import com.commercetools.sunrise.shoppingcart.CartLikeBeanFactory;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.orders.Order;

import javax.inject.Inject;

public class MyOrderDetailPageContentFactory extends Base {

    @Inject
    private CartLikeBeanFactory cartLikeBeanFactory;

    public MyOrderDetailPageContent create(final Order order) {
        final MyOrderDetailPageContent bean = new MyOrderDetailPageContent();
        initialize(bean, order);
        return bean;
    }

    protected final void initialize(final MyOrderDetailPageContent bean, final Order order) {
        fillOrder(bean, order);
    }

    protected void fillOrder(final MyOrderDetailPageContent bean, final Order order) {
        bean.setOrder(cartLikeBeanFactory.create(order));
    }
}
