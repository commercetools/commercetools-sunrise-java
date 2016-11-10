package com.commercetools.sunrise.myaccount.myorders.myorderlist;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.reverserouter.MyOrdersReverseRouter;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifierFactory;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import com.commercetools.sunrise.common.utils.MoneyContext;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.orders.Order;

import javax.inject.Inject;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static com.commercetools.sunrise.common.utils.PriceUtils.calculateTotalPrice;
import static io.sphere.sdk.utils.EnumUtils.enumToCamelCase;

public class OrderOverviewBeanFactory extends Base {

    @Inject
    private UserContext userContext;
    @Inject
    private I18nResolver i18nResolver;
    @Inject
    private I18nIdentifierFactory i18nIdentifierFactory;
    @Inject
    private MyOrdersReverseRouter myOrdersReverseRouter;

    public OrderOverviewBean create(final Order order) {
        final OrderOverviewBean bean = new OrderOverviewBean();
        initialize(bean, order);
        return bean;
    }

    protected final void initialize(final OrderOverviewBean bean, final Order order) {
        fillTotal(bean, order);
        fillOrderDate(bean, order);
        fillShipping(bean, order);
        fillPaymentStatus(bean, order);
        fillOrderNumber(bean, order);
        fillOrderUrl(bean, order);
    }

    protected void fillOrderUrl(final OrderOverviewBean bean, final Order order) {
        bean.setShowOrderUrl(myOrdersReverseRouter.myOrderDetailPageUrlOrEmpty(userContext.locale(), order));
    }

    protected void fillOrderNumber(final OrderOverviewBean bean, final Order order) {
        bean.setOrderNumber(order.getOrderNumber());
    }

    protected void fillTotal(final OrderOverviewBean bean, final Order order) {
        final MoneyContext moneyContext = getMoneyContext(order);
        bean.setTotal(moneyContext.formatOrZero(calculateTotalPrice(order)));
    }

    protected void fillOrderDate(final OrderOverviewBean bean, final Order order) {
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy", userContext.locale());
        bean.setOrderDate(dateTimeFormatter.format(order.getCreatedAt()));
    }

    protected void fillShipping(final OrderOverviewBean bean, final Order order) {
        bean.setShipping(Optional.ofNullable(order.getShipmentState())
                .map(state -> {
                    final String stateName = state.name();
                    final I18nIdentifier i18nIdentifier = i18nIdentifierFactory.create("main:order.shippingStatus." + enumToCamelCase(stateName));
                    return i18nResolver.get(userContext.locales(), i18nIdentifier).orElse(stateName);
                }).orElse("-"));
    }

    protected void fillPaymentStatus(final OrderOverviewBean bean, final Order order) {
        bean.setPaymentStatus(Optional.ofNullable(order.getPaymentState())
                .map(state -> {
                    final String stateName = state.name();
                    final I18nIdentifier i18nIdentifier = i18nIdentifierFactory.create("main:order.paymentStatus." + enumToCamelCase(stateName));
                    return i18nResolver.get(userContext.locales(), i18nIdentifier).orElse(stateName);
                }).orElse("-"));
    }

    protected MoneyContext getMoneyContext(final Order order) {
        return MoneyContext.of(order.getCurrency(), userContext.locale());
    }
}
