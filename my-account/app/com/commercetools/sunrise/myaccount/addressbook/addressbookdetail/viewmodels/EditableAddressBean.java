package com.commercetools.sunrise.myaccount.addressbook.addressbookdetail.viewmodels;

import com.commercetools.sunrise.common.models.ViewModel;
import com.commercetools.sunrise.common.models.addresses.AddressBean;

public class EditableAddressBean extends ViewModel {

    private AddressBean address;
    private String addressEditUrl;
    private String addressDeleteUrl;

    public EditableAddressBean() {
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
