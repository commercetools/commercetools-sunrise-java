package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.ctp.ProductDataConfig;
import com.commercetools.sunrise.common.models.AddressBean;
import com.commercetools.sunrise.common.reverserouter.ProductReverseRouter;
import com.commercetools.sunrise.common.utils.MoneyContext;
import io.sphere.sdk.carts.CartLike;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.orders.Order;

import javax.inject.Inject;
import java.time.format.DateTimeFormatter;

import static com.commercetools.sunrise.common.utils.PriceUtils.calculateSalesTax;
import static com.commercetools.sunrise.common.utils.PriceUtils.calculateSubTotal;
import static com.commercetools.sunrise.common.utils.PriceUtils.calculateTotalPrice;

public class CartLikeBeanFactory extends Base {
    @Inject
    protected UserContext userContext;
    @Inject
    protected ProductDataConfig productDataConfig;
    @Inject
    private ProductReverseRouter productReverseRouter;

    public CartLikeBean create(final CartLike<?> cartLike) {
        final CartLikeBean cartLikeBean = createCartLikeBean();
        final MoneyContext moneyContext = MoneyContext.of(cartLike.getCurrency(), userContext.locale());
        cartLikeBean.setTotalItems(cartLike.getLineItems().stream().mapToLong(LineItem::getQuantity).sum());
        cartLikeBean.setSalesTax(moneyContext.formatOrZero(calculateSalesTax(cartLike).orElse(null)));
        cartLikeBean.setTotalPrice(moneyContext.formatOrZero(calculateTotalPrice(cartLike)));
        cartLikeBean.setSubtotalPrice(moneyContext.formatOrZero(calculateSubTotal(cartLike)));
        cartLikeBean.setLineItems(new LineItemsBean(cartLike.getLineItems(), productDataConfig, userContext, productReverseRouter));
        cartLikeBean.setShippingAddress(new AddressBean(cartLike.getShippingAddress(), userContext.locale()));
        if (cartLike.getBillingAddress() != null) {
            cartLikeBean.setBillingAddress(new AddressBean(cartLike.getBillingAddress(), userContext.locale()));
        } else {
            cartLikeBean.setBillingAddress(new AddressBean(cartLike.getShippingAddress(), userContext.locale()));
        }
        cartLikeBean.setShippingMethod(new ShippingInfoBean(cartLike.getShippingInfo(), moneyContext));
        cartLikeBean.setPaymentDetails(new PaymentInfoBean(cartLike.getPaymentInfo(), userContext));

        if (cartLike instanceof Order) {
            final Order order = (Order) cartLike;
            final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy", userContext.locale());
            cartLikeBean.setOrderDate(dateTimeFormatter.format(order.getCreatedAt()));
            cartLikeBean.setOrderNumber(order.getOrderNumber());
            cartLikeBean.setCustomerEmail(order.getCustomerEmail());
        }
        return cartLikeBean;
    }

    protected CartLikeBean createCartLikeBean() {
        return new CartLikeBean();
    }
}
