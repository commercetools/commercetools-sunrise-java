package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.contexts.UserContext;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.orders.Order;

import javax.inject.Inject;
import java.time.format.DateTimeFormatter;

public class OrderBeanFactory extends CartLikeBeanFactory {

    @Inject
    private UserContext userContext;
    @Inject
    private LineItemExtendedBeanFactory lineItemExtendedBeanFactory;

    public OrderBean create(final Order order) {
        final OrderBean bean = new OrderBean();
        initialize(bean, order);
        return bean;
    }

    protected final void initialize(final OrderBean bean, final Order order) {
        fillCartInfo(bean, order);
        fillOrderDate(bean, order);
        fillOrderNumber(bean, order);
    }

    protected void fillOrderNumber(final OrderBean bean, final Order order) {
        bean.setOrderNumber(order.getOrderNumber());
    }

    protected void fillOrderDate(final OrderBean bean, final Order order) {
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy", userContext.locale());
        bean.setOrderDate(dateTimeFormatter.format(order.getCreatedAt()));
    }

    @Override
    protected LineItemBean createLineItem(final LineItem lineItem) {
        return lineItemExtendedBeanFactory.create(lineItem);
    }
}
