package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.models.AddressBean;

public class CartBean extends MiniCartBean {

    private String customerEmail;
    private AddressBean shippingAddress;
    private AddressBean billingAddress;
    private ShippingInfoBean shippingMethod;
    private PaymentInfoBean paymentDetails;
    private String subtotalPrice;
    private String salesTax;

    public CartBean() {
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
}
