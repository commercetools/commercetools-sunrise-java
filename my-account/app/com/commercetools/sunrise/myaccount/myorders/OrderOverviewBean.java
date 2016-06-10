package com.commercetools.sunrise.myaccount.myorders;

import common.contexts.UserContext;
import common.controllers.ReverseRouter;
import common.template.i18n.I18nIdentifier;
import common.template.i18n.I18nResolver;
import common.utils.MoneyContext;
import io.sphere.sdk.orders.Order;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static io.sphere.sdk.utils.EnumUtils.enumToCamelCase;
import static common.utils.PriceUtils.calculateTotalPrice;

public class OrderOverviewBean {

    private String orderNumber;
    private String date;
    private String total;
    private String paymentStatus;
    private String shipping;
    private String url;

    public OrderOverviewBean() {
    }

    public OrderOverviewBean(final Order order, final UserContext userContext, final I18nResolver i18nResolver,
                             final ReverseRouter reverseRouter) {
        final MoneyContext moneyContext = MoneyContext.of(order.getCurrency(), userContext.locale());
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy", userContext.locale());
        this.orderNumber = order.getOrderNumber();
        this.date = dateTimeFormatter.format(order.getCreatedAt());
        this.total = moneyContext.formatOrZero(calculateTotalPrice(order));
        this.paymentStatus = Optional.ofNullable(order.getPaymentState())
                .map(state -> {
                    final String stateName = state.name();
                    final I18nIdentifier i18nIdentifier = I18nIdentifier.of("my-account:myOrders.paymentStatus." + enumToCamelCase(stateName));
                    return i18nResolver.get(userContext.locales(), i18nIdentifier).orElse(stateName);
                }).orElse("-");
        this.shipping = Optional.ofNullable(order.getShipmentState())
                .map(state -> {
                    final String stateName = state.name();
                    final I18nIdentifier i18nIdentifier = I18nIdentifier.of("my-account:myOrders.shippingStatus." + enumToCamelCase(stateName));
                    return i18nResolver.get(userContext.locales(), i18nIdentifier).orElse(stateName);
                }).orElse("-");
        this.url = reverseRouter.showMyOrderUrlOrEmpty(userContext.locale(), order);
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(final String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(final String date) {
        this.date = date;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(final String total) {
        this.total = total;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(final String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getShipping() {
        return shipping;
    }

    public void setShipping(final String shipping) {
        this.shipping = shipping;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }
}
