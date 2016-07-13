package com.commercetools.sunrise.myaccount.addressbook.addresslist;

import com.commercetools.sunrise.common.models.ModelBean;
import com.commercetools.sunrise.common.models.AddressBean;

public class AddressInfoBean extends ModelBean {

    private AddressBean address;
    private String addressEditUrl;
    private String addressDeleteUrl;

    public AddressInfoBean() {
    }

    public AddressBean getAddress() {
        return address;
    }

    public void setAddress(final AddressBean address) {
        this.address = address;
    }

    public String getAddressEditUrl() {
        return addressEditUrl;
    }

    public void setAddressEditUrl(final String addressEditUrl) {
        this.addressEditUrl = addressEditUrl;
    }

    public String getAddressDeleteUrl() {
        return addressDeleteUrl;
    }

    public void setAddressDeleteUrl(final String addressDeleteUrl) {
        this.addressDeleteUrl = addressDeleteUrl;
    }
}
