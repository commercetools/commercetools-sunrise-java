package com.commercetools.sunrise.myaccount.myorders;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.controllers.ReverseRouter;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import com.commercetools.sunrise.common.utils.MoneyContext;
import io.sphere.sdk.orders.Order;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static io.sphere.sdk.utils.EnumUtils.enumToCamelCase;
import static com.commercetools.sunrise.common.utils.PriceUtils.calculateTotalPrice;

public class OrderOverviewBean {

    private String orderNumber;
    private String orderDate;
    private String total;
    private String paymentStatus;
    private String shipping;
    private String showOrderUrl;

    public OrderOverviewBean() {
    }

    public OrderOverviewBean(final Order order, final UserContext userContext, final I18nResolver i18nResolver,
                             final ReverseRouter reverseRouter) {
        final MoneyContext moneyContext = MoneyContext.of(order.getCurrency(), userContext.locale());
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy", userContext.locale());
        this.orderNumber = order.getOrderNumber();
        this.orderDate = dateTimeFormatter.format(order.getCreatedAt());
        this.total = moneyContext.formatOrZero(calculateTotalPrice(order));
        this.paymentStatus = Optional.ofNullable(order.getPaymentState())
                .map(state -> {
                    final String stateName = state.name();
                    final I18nIdentifier i18nIdentifier = I18nIdentifier.of("main:order.paymentStatus." + enumToCamelCase(stateName));
                    return i18nResolver.get(userContext.locales(), i18nIdentifier).orElse(stateName);
                }).orElse("-");
        this.shipping = Optional.ofNullable(order.getShipmentState())
                .map(state -> {
                    final String stateName = state.name();
                    final I18nIdentifier i18nIdentifier = I18nIdentifier.of("main:order.shippingStatus." + enumToCamelCase(stateName));
                    return i18nResolver.get(userContext.locales(), i18nIdentifier).orElse(stateName);
                }).orElse("-");
        this.showOrderUrl = reverseRouter.showMyOrderUrlOrEmpty(userContext.locale(), order);
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(final String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(final String orderDate) {
        this.orderDate = orderDate;
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

    public String getShowOrderUrl() {
        return showOrderUrl;
    }

    public void setShowOrderUrl(final String showOrderUrl) {
        this.showOrderUrl = showOrderUrl;
    }
}
