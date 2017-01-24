package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.AddressBeanFactory;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.orders.Order;

import javax.money.CurrencyUnit;
import java.time.format.DateTimeFormatter;

@RequestScoped
public class OrderBeanFactory extends AbstractCartBeanFactory<OrderBean, Order> {

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
        final Data<Order> data = new Data<>(order);
        return initializedViewModel(data);
    }

    @Override
    protected OrderBean getViewModelInstance() {
        return new OrderBean();
    }

    @Override
    protected final void initialize(final OrderBean bean, final Data<Order> data) {
        super.initialize(bean, data);
        fillOrderDate(bean, data);
        fillOrderNumber(bean, data);
    }

    protected void fillOrderNumber(final OrderBean bean, final Data<Order> data) {
        if (data.cartLike != null) {
            bean.setOrderNumber(data.cartLike.getOrderNumber());
        }
    }

    protected void fillOrderDate(final OrderBean bean, final Data<Order> data) {
        if (data.cartLike != null) {
            bean.setOrderDate(dateTimeFormatter.format(data.cartLike.getCreatedAt()));
        }
    }

    @Override
    protected final LineItemBean createLineItem(final LineItem lineItem) {
        return lineItemExtendedBeanFactory.create(lineItem);
    }
}
