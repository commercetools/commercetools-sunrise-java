package com.commercetools.sunrise.shoppingcart.checkout.address;

import com.commercetools.sunrise.common.contexts.ProjectContext;
import com.commercetools.sunrise.common.forms.AddressFormBean;
import com.commercetools.sunrise.common.forms.FormBean;
import com.neovisionaries.i18n.CountryCode;

import java.util.List;

public class CheckoutAddressFormBean extends FormBean {

    private boolean differentBillingAddress;
    private AddressFormBean shippingAddress;
    private AddressFormBean billingAddress;

    public boolean isBillingAddressDifferentToBillingAddress() {
        return differentBillingAddress;
    }

    public void setBillingAddressDifferentToBillingAddress(final boolean billingAddressDifferentToBillingAddress) {
        this.differentBillingAddress = billingAddressDifferentToBillingAddress;
    }

    public AddressFormBean getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(final AddressFormBean shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public AddressFormBean getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(final AddressFormBean billingAddress) {
        this.billingAddress = billingAddress;
    }

    private List<CountryCode> availableBillingCountries(final ProjectContext projectContext) {
        return projectContext.countries();
    }
}
