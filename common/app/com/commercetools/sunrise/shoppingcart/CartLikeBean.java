package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.ctp.ProductDataConfig;
import com.commercetools.sunrise.common.models.AddressBean;
import com.commercetools.sunrise.common.utils.MoneyContext;
import io.sphere.sdk.carts.CartLike;
import io.sphere.sdk.carts.LineItem;
import io.sphere.sdk.orders.Order;
import com.commercetools.sunrise.common.reverserouter.ProductReverseRouter;

import java.time.format.DateTimeFormatter;

import static com.commercetools.sunrise.common.utils.PriceUtils.*;

public class CartLikeBean {

    private String orderNumber;
    private String orderDate;
    private String customerEmail;
    private AddressBean shippingAddress;
    private AddressBean billingAddress;
    private ShippingInfoBean shippingMethod;
    private PaymentInfoBean paymentDetails;
    private String subtotalPrice;
    private String salesTax;
    private String totalPrice;
    private Long totalItems;
    private LineItemsBean lineItems;

    public CartLikeBean() {
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

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(final String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public AddressBean getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(final AddressBean shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public AddressBean getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(final AddressBean billingAddress) {
        this.billingAddress = billingAddress;
    }

    public ShippingInfoBean getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(final ShippingInfoBean shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public PaymentInfoBean getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(final PaymentInfoBean paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public String getSubtotalPrice() {
        return subtotalPrice;
    }

    public void setSubtotalPrice(final String subtotalPrice) {
        this.subtotalPrice = subtotalPrice;
    }

    public String getSalesTax() {
        return salesTax;
    }

    public void setSalesTax(final String salesTax) {
        this.salesTax = salesTax;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(final String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Long getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(final Long totalItems) {
        this.totalItems = totalItems;
    }

    public LineItemsBean getLineItems() {
        return lineItems;
    }

    public void setLineItems(final LineItemsBean lineItems) {
        this.lineItems = lineItems;
    }
}
