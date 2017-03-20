package com.commercetools.sunrise.myaccount.myorders.myorderlist.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.SimpleViewModelFactory;
import com.commercetools.sunrise.framework.viewmodels.formatters.PriceFormatter;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.reverserouters.myaccount.myorders.MyOrdersReverseRouter;
import com.commercetools.sunrise.framework.template.i18n.I18nIdentifierResolver;
import io.sphere.sdk.orders.Order;
import play.mvc.Call;

import javax.inject.Inject;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.commercetools.sunrise.framework.viewmodels.content.carts.CartPriceUtils.calculateTotalPrice;
import static com.commercetools.sunrise.ctp.CtpEnumUtils.enumToCamelCase;

@RequestScoped
public class OrderOverviewViewModelFactory extends SimpleViewModelFactory<OrderOverviewViewModel, Order> {

    private final I18nIdentifierResolver i18nIdentifierResolver;
    private final PriceFormatter priceFormatter;
    private final DateTimeFormatter dateTimeFormatter;
    private final MyOrdersReverseRouter myOrdersReverseRouter;

    @Inject
    public OrderOverviewViewModelFactory(final I18nIdentifierResolver i18nIdentifierResolver, final PriceFormatter priceFormatter,
                                         final DateTimeFormatter dateTimeFormatter, final MyOrdersReverseRouter myOrdersReverseRouter) {
        this.i18nIdentifierResolver = i18nIdentifierResolver;
        this.priceFormatter = priceFormatter;
        this.dateTimeFormatter = dateTimeFormatter;
        this.myOrdersReverseRouter = myOrdersReverseRouter;
    }

    protected final I18nIdentifierResolver getI18nIdentifierResolver() {
        return i18nIdentifierResolver;
    }

    protected final PriceFormatter getPriceFormatter() {
        return priceFormatter;
    }

    protected final DateTimeFormatter getDateTimeFormatter() {
        return dateTimeFormatter;
    }

    protected final MyOrdersReverseRouter getMyOrdersReverseRouter() {
        return myOrdersReverseRouter;
    }

    @Override
    protected OrderOverviewViewModel newViewModelInstance(final Order order) {
        return new OrderOverviewViewModel();
    }

    @Override
    public final OrderOverviewViewModel create(final Order order) {
        return super.create(order);
    }

    protected final void initialize(final OrderOverviewViewModel viewModel, final Order order) {
        fillTotal(viewModel, order);
        fillOrderDate(viewModel, order);
        fillShipping(viewModel, order);
        fillPaymentStatus(viewModel, order);
        fillOrderNumber(viewModel, order);
        fillOrderUrl(viewModel, order);
    }

    protected void fillOrderUrl(final OrderOverviewViewModel viewModel, final Order order) {
        viewModel.setShowOrderUrl(myOrdersReverseRouter
                .myOrderDetailPageCall(order)
                .map(Call::url)
                .orElse(""));
    }

    protected void fillOrderNumber(final OrderOverviewViewModel viewModel, final Order order) {
        viewModel.setOrderNumber(order.getOrderNumber());
    }

    protected void fillTotal(final OrderOverviewViewModel viewModel, final Order order) {
        viewModel.setTotal(priceFormatter.format(calculateTotalPrice(order)));
    }

    protected void fillOrderDate(final OrderOverviewViewModel viewModel, final Order order) {
        viewModel.setOrderDate(dateTimeFormatter.format(order.getCreatedAt()));
    }

    protected void fillShipping(final OrderOverviewViewModel viewModel, final Order order) {
        viewModel.setShipping(Optional.ofNullable(order.getShipmentState())
                .map(state -> {
                    final String stateName = state.name();
                    return i18nIdentifierResolver.resolve("main:order.shippingStatus." + enumToCamelCase(stateName))
                            .orElse(stateName);
                }).orElse("-"));
    }

    protected void fillPaymentStatus(final OrderOverviewViewModel viewModel, final Order order) {
        viewModel.setPaymentStatus(Optional.ofNullable(order.getPaymentState())
                .map(state -> {
                    final String stateName = state.name();
                    return i18nIdentifierResolver.resolve("main:order.paymentStatus." + enumToCamelCase(stateName))
                            .orElse(stateName);
                }).orElse("-"));
    }
}
