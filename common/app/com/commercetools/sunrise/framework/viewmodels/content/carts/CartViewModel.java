package com.commercetools.sunrise.framework.viewmodels.content.carts;

import com.commercetools.sunrise.framework.viewmodels.content.addresses.AddressViewModel;

public class CartViewModel extends MiniCartViewModel {

    private String customerEmail;
    private AddressViewModel shippingAddress;
    private AddressViewModel billingAddress;
    private ShippingInfoViewModel shippingMethod;
    private PaymentInfoViewModel paymentDetails;
    private String subtotalPrice;
    private String salesTax;

    public CartViewModel() {
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(final String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public AddressViewModel getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(final AddressViewModel shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public AddressViewModel getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(final AddressViewModel billingAddress) {
        this.billingAddress = billingAddress;
    }

    public ShippingInfoViewModel getShippingMethod() {
        return shippingMethod;
    }

    public void setShippingMethod(final ShippingInfoViewModel shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public PaymentInfoViewModel getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(final PaymentInfoViewModel paymentDetails) {
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
