package com.commercetools.sunrise.myaccount.addressbook.addressbookdetail.viewmodels;

import com.commercetools.sunrise.framework.viewmodels.ViewModel;
import com.commercetools.sunrise.framework.viewmodels.content.addresses.AddressViewModel;

public class EditableAddressViewModel extends ViewModel {

    private AddressViewModel address;
    private String addressEditUrl;
    private String addressDeleteUrl;

    public EditableAddressViewModel() {
    }

    public AddressViewModel getAddress() {
        return address;
    }

    public void setAddress(final AddressViewModel address) {
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
