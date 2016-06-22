package com.commercetools.sunrise.myaccount.addressbook.removeaddress;

import io.sphere.sdk.models.Base;

public class RemoveAddressFormData extends Base {

    private String csrfToken;

    public String getCsrfToken() {
        return csrfToken;
    }

    public void setCsrfToken(final String csrfToken) {
        this.csrfToken = csrfToken;
    }
}
