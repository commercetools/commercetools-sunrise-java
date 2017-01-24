package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.AddressBeanFactory;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.orders.Order;

import javax.money.CurrencyUnit;
import java.time.format.DateTimeFormatter;

@RequestScoped
public class OrderBeanFactory extends AbstractCartLikeBeanFactory<OrderBean, OrderBeanFactory.Data, Order> {

    private final DateTimeFormatter dateTimeFormatter;
    private final LineItemExtendedBeanFactory lineItemExtendedBeanFactory;

    public OrderBeanFactory(final CurrencyUnit currency, final PriceFormatter priceFormatter, final DateTimeFormatter dateTimeFormatter,
                            final ShippingInfoBeanFactory shippingInfoBeanFactory, final PaymentInfoBeanFactory paymentInfoBeanFactory,
                            final AddressBeanFactory addressBeanFactory, final LineItemExtendedBeanFactory lineItemExtendedBeanFactory) {
        super(currency, priceFormatter, shippingInfoBeanFactory, paymentInfoBeanFactory, addressBeanFactory);
        this.dateTimeFormatter = dateTimeFormatter;
        this.lineItemExtendedBeanFactory = lineItemExtendedBeanFactory;
    }

    public final OrderBean create(final Order order) {
        final Data data = new Data(order);
        return initializedViewModel(data);
    }

    @Override
    protected OrderBean getViewModelInstance() {
        return new OrderBean();
    }

    @Override
    protected final void initialize(final OrderBean bean, final Data data) {
        super.initialize(bean, data);
        fillOrderDate(bean, data);
        fillOrderNumber(bean, data);
    }

    protected void fillOrderNumber(final OrderBean bean, final Data data) {
        bean.setOrderNumber(data.order.getOrderNumber());
    }

    protected void fillOrderDate(final OrderBean bean, final Data data) {
        bean.setOrderDate(dateTimeFormatter.format(data.order.getCreatedAt()));
    }

    @Override
    LineItemBean createLineItem(final LineItem lineItem) {
        return lineItemExtendedBeanFactory.create(lineItem);
    }

    protected final static class Data extends AbstractMiniCartBeanFactory.Data<Order> {

        public final Order order;

        public Data(final Order order) {
            super(order);
            this.order = order;
        }
    }
}
