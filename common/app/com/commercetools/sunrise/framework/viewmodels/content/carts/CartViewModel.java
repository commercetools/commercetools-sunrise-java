package com.commercetools.sunrise.framework.viewmodels.content.carts;

import com.commercetools.sunrise.framework.viewmodels.content.addresses.AddressViewModel;
import play.data.Form;

import java.util.List;

public class CartViewModel extends MiniCartViewModel {

    private String customerEmail;
    private AddressViewModel shippingAddress;
    private AddressViewModel billingAddress;
    private ShippingInfoViewModel shippingMethod;
    private PaymentInfoViewModel paymentDetails;
    private String subtotalPrice;
    private String salesTax;
    private List<DiscountCodeViewModel> discountCodes;
    private Form<?> addDiscountCodeForm;

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

    public List<DiscountCodeViewModel> getDiscountCodes() {
        return discountCodes;
    }

    public void setDiscountCodes(final List<DiscountCodeViewModel> discountCodes) {
        this.discountCodes = discountCodes;
    }

    public Form<?> getAddDiscountCodeForm() {
        return addDiscountCodeForm;
    }

    public void setAddDiscountCodeForm(final Form<?> addDiscountCodeForm) {
        this.addDiscountCodeForm = addDiscountCodeForm;
    }
}
