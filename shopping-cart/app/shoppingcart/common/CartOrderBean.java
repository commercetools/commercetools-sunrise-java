package shoppingcart.common;

import common.contexts.UserContext;
import common.controllers.ReverseRouter;
import common.models.ProductDataConfig;
import common.utils.MoneyContext;
import io.sphere.sdk.carts.CartLike;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.orders.Order;
import shoppingcart.checkout.address.AddressBean;
import shoppingcart.checkout.shipping.ShippingMethodBean;

import java.time.format.DateTimeFormatter;

import static common.utils.PriceUtils.*;

public class CartOrderBean {

    private Long totalItems;
    private String salesTax;
    private String totalPrice;
    private String subtotalPrice;
    private CartLineItemsBean lineItems;
    private AddressBean shippingAddress;
    private AddressBean billingAddress;
    private ShippingMethodBean shippingMethod;
    private PaymentBean paymentDetails;
    private String orderNumber;
    private String orderDate;
    private String customerEmail;

    public CartOrderBean() {
    }

    public CartOrderBean(final CartLike<?> cartLike, final UserContext userContext,
                         final ProductDataConfig productDataConfig, final ReverseRouter reverseRouter) {
        final MoneyContext moneyContext = MoneyContext.of(cartLike.getCurrency(), userContext.locale());
        this.totalItems = cartLike.getLineItems().stream().mapToLong(LineItem::getQuantity).sum();
        this.salesTax = moneyContext.formatOrZero(calculateSalesTax(cartLike).orElse(null));
        this.totalPrice = moneyContext.formatOrZero(calculateTotalPrice(cartLike));
        this.subtotalPrice = moneyContext.formatOrZero(calculateSubTotal(cartLike));
        this.lineItems = new CartLineItemsBean(cartLike, productDataConfig, userContext, reverseRouter);
        this.shippingAddress = new AddressBean(cartLike.getShippingAddress(), userContext.locale());
        this.billingAddress = new AddressBean(cartLike.getBillingAddress(), userContext.locale());
        this.shippingMethod = new ShippingMethodBean(cartLike, moneyContext);
        this.paymentDetails = new PaymentBean("prepaid");

        if (cartLike instanceof Order) {
            fillOrder((Order) cartLike, userContext);
        }
    }

    private void fillOrder(final Order order, final UserContext userContext) {
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy", userContext.locale());
        this.orderDate = dateTimeFormatter.format(order.getCreatedAt());
        this.orderNumber = order.getOrderNumber();
        this.customerEmail = order.getCustomerEmail();
    }

    public Long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(final Long totalItems) {
        this.totalItems = totalItems;
    }

    public CartLineItemsBean getLineItems() {
        return lineItems;
    }

    public void setLineItems(final CartLineItemsBean lineItems) {
        this.lineItems = lineItems;
    }

    public String getSalesTax() {
        return salesTax;
    }

    public void setSalesTax(final String salesTax) {
        this.salesTax = salesTax;
    }

    public String getSubtotalPrice() {
        return subtotalPrice;
    }

    public void setSubtotalPrice(final String subtotalPrice) {
        this.subtotalPrice = subtotalPrice;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(final String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public AddressBean getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(final AddressBean billingAddress) {
        this.billingAddress = billingAddress;
    }

    public AddressBean getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(final AddressBean shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public ShippingMethodBean getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(final ShippingMethodBean shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public PaymentBean getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(final PaymentBean paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(final String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(final String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(final String orderNumber) {
        this.orderNumber = orderNumber;
    }
}
