package com.commercetools.sunrise.myaccount.myorders.myorderlist;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.models.ViewModelFactory;
import com.commercetools.sunrise.common.reverserouter.MyOrdersReverseRouter;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifier;
import com.commercetools.sunrise.common.template.i18n.I18nIdentifierFactory;
import com.commercetools.sunrise.common.template.i18n.I18nResolver;
import com.commercetools.sunrise.common.utils.PriceFormatter;
import io.sphere.sdk.orders.Order;

import javax.inject.Inject;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static com.commercetools.sunrise.common.utils.CartPriceUtils.calculateTotalPrice;
import static io.sphere.sdk.utils.EnumUtils.enumToCamelCase;

@RequestScoped
public class OrderOverviewBeanFactory extends ViewModelFactory {

    private final Locale locale;
    private final List<Locale> locales;
    private final PriceFormatter priceFormatter;
    private final I18nResolver i18nResolver;
    private final I18nIdentifierFactory i18nIdentifierFactory;
    private final MyOrdersReverseRouter myOrdersReverseRouter;

    @Inject
    public OrderOverviewBeanFactory(final UserContext userContext, final PriceFormatter priceFormatter,
                                    final I18nResolver i18nResolver, final I18nIdentifierFactory i18nIdentifierFactory,
                                    final MyOrdersReverseRouter myOrdersReverseRouter) {
        this.locale = userContext.locale();
        this.locales = userContext.locales();
        this.priceFormatter = priceFormatter;
        this.i18nResolver = i18nResolver;
        this.i18nIdentifierFactory = i18nIdentifierFactory;
        this.myOrdersReverseRouter = myOrdersReverseRouter;
    }

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
        bean.setShowOrderUrl(myOrdersReverseRouter.myOrderDetailPageUrlOrEmpty(locale, order));
    }

    protected void fillOrderNumber(final OrderOverviewBean bean, final Order order) {
        bean.setOrderNumber(order.getOrderNumber());
    }

    protected void fillTotal(final OrderOverviewBean bean, final Order order) {
        bean.setTotal(priceFormatter.format(calculateTotalPrice(order)));
    }

    protected void fillOrderDate(final OrderOverviewBean bean, final Order order) {
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy", locale);
        bean.setOrderDate(dateTimeFormatter.format(order.getCreatedAt()));
    }

    protected void fillShipping(final OrderOverviewBean bean, final Order order) {
        bean.setShipping(Optional.ofNullable(order.getShipmentState())
                .map(state -> {
                    final String stateName = state.name();
                    final I18nIdentifier i18nIdentifier = i18nIdentifierFactory.create("main:order.shippingStatus." + enumToCamelCase(stateName));
                    return i18nResolver.get(locales, i18nIdentifier).orElse(stateName);
                }).orElse("-"));
    }

    protected void fillPaymentStatus(final OrderOverviewBean bean, final Order order) {
        bean.setPaymentStatus(Optional.ofNullable(order.getPaymentState())
                .map(state -> {
                    final String stateName = state.name();
                    final I18nIdentifier i18nIdentifier = i18nIdentifierFactory.create("main:order.paymentStatus." + enumToCamelCase(stateName));
                    return i18nResolver.get(locales, i18nIdentifier).orElse(stateName);
                }).orElse("-"));
    }
}
