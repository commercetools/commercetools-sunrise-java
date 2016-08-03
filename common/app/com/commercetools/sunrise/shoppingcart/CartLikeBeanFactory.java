package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.ctp.ProductDataConfig;
import com.commercetools.sunrise.common.models.AddressBeanFactory;
import com.commercetools.sunrise.common.utils.MoneyContext;
import io.sphere.sdk.carts.CartLike;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.orders.Order;

import javax.inject.Inject;
import java.time.format.DateTimeFormatter;

import static com.commercetools.sunrise.common.utils.PriceUtils.*;

public class CartLikeBeanFactory extends Base {

    @Inject
    protected UserContext userContext;
    @Inject
    protected ProductDataConfig productDataConfig;
    @Inject
    private AddressBeanFactory addressBeanFactory;
    @Inject
    private LineItemsBeanFactory lineItemsBeanFactory;

    public CartLikeBean create(final CartLike<?> cartLike) {
        final CartLikeBean bean = new CartLikeBean();
        initialize(bean, cartLike);
        return bean;
    }

    public CartLikeBean createWithEmptyCart(){
        final CartLikeBean bean = new CartLikeBean();
        initializeWithEmptyCart(bean);
        return bean;
    }

    protected final void initialize(final CartLikeBean bean, final CartLike<?> cartLike) {
        fillPriceInfo(bean, cartLike);
        fillTotalItems(bean, cartLike);
        fillLineItems(bean, cartLike);
        fillShippingAddress(bean, cartLike);
        fillBillingAddress(bean, cartLike);
        fillShippingMethod(bean, cartLike);
        fillPaymentDetails(bean, cartLike);
        if (cartLike instanceof Order) {
            fillOrderInfo(bean, (Order) cartLike);
        }
    }

    protected final void initializeWithEmptyCart(final CartLikeBean bean) {
        bean.setTotalItems(0L);
        final MoneyContext moneyContext = MoneyContext.of(userContext.currency(), userContext.locale());
        bean.setSalesTax(moneyContext.formatOrZero(null));
        bean.setTotalPrice(moneyContext.formatOrZero(null));
        bean.setSubtotalPrice(moneyContext.formatOrZero(null));
    }

    protected void fillTotalItems(final CartLikeBean bean, final CartLike<?> cartLike) {
        bean.setTotalItems(cartLike.getLineItems().stream().mapToLong(LineItem::getQuantity).sum());
    }

    protected void fillLineItems(final CartLikeBean bean, final CartLike<?> cartLike) {
        bean.setLineItems(lineItemsBeanFactory.create(cartLike.getLineItems()));
    }

    protected void fillPriceInfo(final CartLikeBean bean, final CartLike<?> cartLike) {
        final MoneyContext moneyContext = getMoneyContext(cartLike);
        bean.setSalesTax(moneyContext.formatOrZero(calculateSalesTax(cartLike).orElse(null)));
        bean.setTotalPrice(moneyContext.formatOrZero(calculateTotalPrice(cartLike)));
        bean.setSubtotalPrice(moneyContext.formatOrZero(calculateSubTotal(cartLike)));
    }

    protected void fillPaymentDetails(final CartLikeBean bean, final CartLike<?> cartLike) {
        bean.setPaymentDetails(new PaymentInfoBean(cartLike.getPaymentInfo(), userContext));
    }

    protected void fillShippingMethod(final CartLikeBean bean, final CartLike<?> cartLike) {
        final MoneyContext moneyContext = getMoneyContext(cartLike);
        bean.setShippingMethod(new ShippingInfoBean(cartLike.getShippingInfo(), moneyContext));
    }

    protected void fillShippingAddress(final CartLikeBean bean, final CartLike<?> cartLike) {
        bean.setShippingAddress(addressBeanFactory.create(cartLike.getShippingAddress()));
    }

    protected void fillBillingAddress(final CartLikeBean bean, final CartLike<?> cartLike) {
        if (cartLike.getBillingAddress() != null) {
            bean.setBillingAddress(addressBeanFactory.create(cartLike.getBillingAddress()));
        } else {
            bean.setBillingAddress(addressBeanFactory.create(cartLike.getShippingAddress()));
        }
    }

    protected void fillOrderInfo(final CartLikeBean bean, final Order order) {
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy", userContext.locale());
        bean.setOrderDate(dateTimeFormatter.format(order.getCreatedAt()));
        bean.setOrderNumber(order.getOrderNumber());
        bean.setCustomerEmail(order.getCustomerEmail());
    }

    protected MoneyContext getMoneyContext(final CartLike<?> cartLike) {
        return MoneyContext.of(cartLike.getCurrency(), userContext.locale());
    }
}
