package com.commercetools.sunrise.myaccount.addressbook;

import io.sphere.sdk.models.Base;
import play.data.validation.Constraints;

public class RemoveAddressFormData extends Base {

    private String csrfToken;

    @Constraints.Required
    private String addressId;

    public String getCsrfToken() {
        return csrfToken;
    }

    public void setCsrfToken(final String csrfToken) {
        this.csrfToken = csrfToken;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(final String addressId) {
        this.addressId = addressId;
    }
}
