package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.AddressBeanFactory;
import com.commercetools.sunrise.common.utils.LocalizedStringResolver;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.orders.Order;

import java.time.format.DateTimeFormatter;

@RequestScoped
public class OrderBeanFactory extends CartLikeBeanFactory {

    private final DateTimeFormatter dateTimeFormatter;
    private final LocalizedStringResolver localizedStringResolver;
    private final AddressBeanFactory addressBeanFactory;
    private final LineItemExtendedBeanFactory lineItemExtendedBeanFactory;

    public OrderBeanFactory(final PriceFormatter priceFormatter, final DateTimeFormatter dateTimeFormatter, final LocalizedStringResolver localizedStringResolver,
                            final AddressBeanFactory addressBeanFactory, final LineItemExtendedBeanFactory lineItemExtendedBeanFactory) {
        super(priceFormatter);
        this.dateTimeFormatter = dateTimeFormatter;
        this.localizedStringResolver = localizedStringResolver;
        this.addressBeanFactory = addressBeanFactory;
        this.lineItemExtendedBeanFactory = lineItemExtendedBeanFactory;
    }

    public OrderBean create(final Order order) {
        final OrderBean bean = new OrderBean();
        initialize(bean, order);
        return bean;
    }

    protected final void initialize(final OrderBean bean, final Order order) {
        fillCartInfo(bean, order, localizedStringResolver, addressBeanFactory);
        fillOrderDate(bean, order);
        fillOrderNumber(bean, order);
    }

    protected void fillOrderNumber(final OrderBean bean, final Order order) {
        bean.setOrderNumber(order.getOrderNumber());
    }

    protected void fillOrderDate(final OrderBean bean, final Order order) {
        bean.setOrderDate(dateTimeFormatter.format(order.getCreatedAt()));
    }

    @Override
    protected LineItemBean createLineItem(final LineItem lineItem) {
        return lineItemExtendedBeanFactory.create(lineItem);
    }
}
