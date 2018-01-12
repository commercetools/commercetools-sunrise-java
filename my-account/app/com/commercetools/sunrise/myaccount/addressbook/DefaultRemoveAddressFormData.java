package com.commercetools.sunrise.myaccount.addressbook;

import io.sphere.sdk.models.Base;
import play.data.validation.Constraints;

public class DefaultRemoveAddressFormData extends Base implements RemoveAddressFormData {

    @Constraints.Required
    private String addressId;

    @Override
    public String addressId() {
        return addressId;
    }
}
