package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.models.AddressBeanFactory;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.orders.Order;

import javax.inject.Inject;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@RequestScoped
public class OrderBeanFactory extends CartLikeBeanFactory {

    private final Locale locale;
    private final LineItemExtendedBeanFactory lineItemExtendedBeanFactory;

    @Inject
    public OrderBeanFactory(final UserContext userContext, final PriceFormatter priceFormatter,
                            final AddressBeanFactory addressBeanFactory,
                            final LineItemExtendedBeanFactory lineItemExtendedBeanFactory) {
        super(userContext, priceFormatter, addressBeanFactory);
        this.locale = userContext.locale();
        this.lineItemExtendedBeanFactory = lineItemExtendedBeanFactory;
    }

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
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy", locale);
        bean.setOrderDate(dateTimeFormatter.format(order.getCreatedAt()));
    }

    @Override
    protected LineItemBean createLineItem(final LineItem lineItem) {
        return lineItemExtendedBeanFactory.create(lineItem);
    }
}
