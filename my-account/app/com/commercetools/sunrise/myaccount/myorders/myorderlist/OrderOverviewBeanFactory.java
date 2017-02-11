package com.commercetools.sunrise.myaccount.myorders.myorderlist;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.common.reverserouter.MyOrdersReverseRouter;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifierFactory;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import io.sphere.sdk.orders.Order;

import javax.inject.Inject;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

import static com.commercetools.sunrise.common.utils.CartPriceUtils.calculateTotalPrice;
import static io.sphere.sdk.utils.EnumUtils.enumToCamelCase;
import static java.util.Collections.singletonList;

@RequestScoped
public class OrderOverviewBeanFactory extends ViewModelFactory<OrderOverviewBean, Order> {

    private final Locale locale;
    private final PriceFormatter priceFormatter;
    private final DateTimeFormatter dateTimeFormatter;
    private final I18nResolver i18nResolver;
    private final I18nIdentifierFactory i18nIdentifierFactory;
    private final MyOrdersReverseRouter myOrdersReverseRouter;

    @Inject
    public OrderOverviewBeanFactory(final Locale locale, final PriceFormatter priceFormatter, final DateTimeFormatter dateTimeFormatter,
                                    final I18nResolver i18nResolver, final I18nIdentifierFactory i18nIdentifierFactory,
                                    final MyOrdersReverseRouter myOrdersReverseRouter) {
        this.locale = locale;
        this.priceFormatter = priceFormatter;
        this.dateTimeFormatter = dateTimeFormatter;
        this.i18nResolver = i18nResolver;
        this.i18nIdentifierFactory = i18nIdentifierFactory;
        this.myOrdersReverseRouter = myOrdersReverseRouter;
    }

    @Override
    protected OrderOverviewBean getViewModelInstance() {
        return new OrderOverviewBean();
    }

    @Override
    public final OrderOverviewBean create(final Order data) {
        return super.create(data);
    }

    protected final void initialize(final OrderOverviewBean model, final Order data) {
        fillTotal(model, data);
        fillOrderDate(model, data);
        fillShipping(model, data);
        fillPaymentStatus(model, data);
        fillOrderNumber(model, data);
        fillOrderUrl(model, data);
    }

    protected void fillOrderUrl(final OrderOverviewBean model, final Order order) {
        model.setShowOrderUrl(myOrdersReverseRouter.myOrderDetailPageUrlOrEmpty(order));
    }

    protected void fillOrderNumber(final OrderOverviewBean model, final Order order) {
        model.setOrderNumber(order.getOrderNumber());
    }

    protected void fillTotal(final OrderOverviewBean model, final Order order) {
        model.setTotal(priceFormatter.format(calculateTotalPrice(order)));
    }

    protected void fillOrderDate(final OrderOverviewBean model, final Order order) {
        model.setOrderDate(dateTimeFormatter.format(order.getCreatedAt()));
    }

    protected void fillShipping(final OrderOverviewBean model, final Order order) {
        model.setShipping(Optional.ofNullable(order.getShipmentState())
                .map(state -> {
                    final String stateName = state.name();
                    final I18nIdentifier i18nIdentifier = i18nIdentifierFactory.create("main:order.shippingStatus." + enumToCamelCase(stateName));
                    return i18nResolver.get(singletonList(locale), i18nIdentifier).orElse(stateName);
                }).orElse("-"));
    }

    protected void fillPaymentStatus(final OrderOverviewBean model, final Order order) {
        model.setPaymentStatus(Optional.ofNullable(order.getPaymentState())
                .map(state -> {
                    final String stateName = state.name();
                    final I18nIdentifier i18nIdentifier = i18nIdentifierFactory.create("main:order.paymentStatus." + enumToCamelCase(stateName));
                    return i18nResolver.get(singletonList(locale), i18nIdentifier).orElse(stateName);
                }).orElse("-"));
    }
}
