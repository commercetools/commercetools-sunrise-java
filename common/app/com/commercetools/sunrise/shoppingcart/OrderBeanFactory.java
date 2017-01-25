package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.AddressBeanFactory;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.orders.Order;

import javax.money.CurrencyUnit;
import java.time.format.DateTimeFormatter;

@RequestScoped
public class OrderBeanFactory extends AbstractCartLikeBeanFactory<OrderBean, Order> {

    private final DateTimeFormatter dateTimeFormatter;
    private final LineItemExtendedBeanFactory lineItemExtendedBeanFactory;

    public OrderBeanFactory(final CurrencyUnit currency, final PriceFormatter priceFormatter, final DateTimeFormatter dateTimeFormatter,
                            final ShippingInfoBeanFactory shippingInfoBeanFactory, final PaymentInfoBeanFactory paymentInfoBeanFactory,
                            final AddressBeanFactory addressBeanFactory, final LineItemExtendedBeanFactory lineItemExtendedBeanFactory) {
        super(currency, priceFormatter, shippingInfoBeanFactory, paymentInfoBeanFactory, addressBeanFactory);
        this.dateTimeFormatter = dateTimeFormatter;
        this.lineItemExtendedBeanFactory = lineItemExtendedBeanFactory;
    }

    @Override
    protected OrderBean getViewModelInstance() {
        return new OrderBean();
    }

    @Override
    public final OrderBean create(final Order data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final OrderBean model, final Order data) {
        super.initialize(model, data);
        fillOrderDate(model, data);
        fillOrderNumber(model, data);
    }

    protected void fillOrderNumber(final OrderBean bean, final Order order) {
        bean.setOrderNumber(order.getOrderNumber());
    }

    protected void fillOrderDate(final OrderBean bean, final Order order) {
        bean.setOrderDate(dateTimeFormatter.format(order.getCreatedAt()));
    }

    @Override
    protected void fillSalesTax(final OrderBean bean, final Order cartLike) {
        super.fillSalesTax(bean, cartLike);
    }

    @Override
    protected void fillSubtotalPrice(final OrderBean bean, final Order cartLike) {
        super.fillSubtotalPrice(bean, cartLike);
    }

    @Override
    protected void fillCustomerEmail(final OrderBean bean, final Order cartLike) {
        super.fillCustomerEmail(bean, cartLike);
    }

    @Override
    protected void fillPaymentDetails(final OrderBean bean, final Order cartLike) {
        super.fillPaymentDetails(bean, cartLike);
    }

    @Override
    protected void fillShippingMethod(final OrderBean bean, final Order cartLike) {
        super.fillShippingMethod(bean, cartLike);
    }

    @Override
    protected void fillShippingAddress(final OrderBean bean, final Order cartLike) {
        super.fillShippingAddress(bean, cartLike);
    }

    @Override
    protected void fillBillingAddress(final OrderBean bean, final Order cartLike) {
        super.fillBillingAddress(bean, cartLike);
    }

    @Override
    LineItemBean createLineItem(final LineItem lineItem) {
        return lineItemExtendedBeanFactory.create(lineItem);
    }
}
