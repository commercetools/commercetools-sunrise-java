package com.commercetools.sunrise.common.models.carts;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.common.models.addresses.AddressViewModelFactory;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.orders.Order;

import javax.inject.Inject;
import javax.money.CurrencyUnit;
import java.time.format.DateTimeFormatter;

@RequestScoped
public class OrderViewModelFactory extends AbstractCartLikeViewModelFactory<OrderViewModel, Order> {

    private final DateTimeFormatter dateTimeFormatter;
    private final LineItemExtendedViewModelFactory lineItemExtendedViewModelFactory;

    @Inject
    public OrderViewModelFactory(final CurrencyUnit currency, final PriceFormatter priceFormatter, final DateTimeFormatter dateTimeFormatter,
                                 final ShippingInfoViewModelFactory shippingInfoViewModelFactory, final PaymentInfoViewModelFactory paymentInfoViewModelFactory,
                                 final AddressViewModelFactory addressViewModelFactory, final LineItemExtendedViewModelFactory lineItemExtendedViewModelFactory) {
        super(currency, priceFormatter, shippingInfoViewModelFactory, paymentInfoViewModelFactory, addressViewModelFactory);
        this.dateTimeFormatter = dateTimeFormatter;
        this.lineItemExtendedViewModelFactory = lineItemExtendedViewModelFactory;
    }

    @Override
    protected OrderViewModel getViewModelInstance() {
        return new OrderViewModel();
    }

    @Override
    public final OrderViewModel create(final Order order) {
        return super.create(order);
    }

    @Override
    protected final void initialize(final OrderViewModel viewModel, final Order order) {
        super.initialize(viewModel, order);
        fillOrderDate(viewModel, order);
        fillOrderNumber(viewModel, order);
    }

    protected void fillOrderNumber(final OrderViewModel viewModel, final Order order) {
        viewModel.setOrderNumber(order.getOrderNumber());
    }

    protected void fillOrderDate(final OrderViewModel viewModel, final Order order) {
        viewModel.setOrderDate(dateTimeFormatter.format(order.getCreatedAt()));
    }

    @Override
    protected void fillSalesTax(final OrderViewModel viewModel, final Order cartLike) {
        super.fillSalesTax(viewModel, cartLike);
    }

    @Override
    protected void fillSubtotalPrice(final OrderViewModel viewModel, final Order cartLike) {
        super.fillSubtotalPrice(viewModel, cartLike);
    }

    @Override
    protected void fillCustomerEmail(final OrderViewModel viewModel, final Order cartLike) {
        super.fillCustomerEmail(viewModel, cartLike);
    }

    @Override
    protected void fillPaymentDetails(final OrderViewModel viewModel, final Order cartLike) {
        super.fillPaymentDetails(viewModel, cartLike);
    }

    @Override
    protected void fillShippingMethod(final OrderViewModel viewModel, final Order cartLike) {
        super.fillShippingMethod(viewModel, cartLike);
    }

    @Override
    protected void fillShippingAddress(final OrderViewModel viewModel, final Order cartLike) {
        super.fillShippingAddress(viewModel, cartLike);
    }

    @Override
    protected void fillBillingAddress(final OrderViewModel viewModel, final Order cartLike) {
        super.fillBillingAddress(viewModel, cartLike);
    }

    @Override
    LineItemViewModel createLineItem(final LineItem lineItem) {
        return lineItemExtendedViewModelFactory.create(lineItem);
    }
}
