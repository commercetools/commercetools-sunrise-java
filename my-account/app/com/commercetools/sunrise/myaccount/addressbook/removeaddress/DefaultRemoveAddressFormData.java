package com.commercetools.sunrise.myaccount.addressbook.removeaddress;

import io.sphere.sdk.models.Base;

public class DefaultRemoveAddressFormData extends Base implements RemoveAddressFormData {

    private String addressId;

    @Override
    public String addressId() {
        return addressId;
    }

    // Getters & setters

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(final String addressId) {
        this.addressId = addressId;
    }
}
