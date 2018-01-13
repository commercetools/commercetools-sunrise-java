package com.commercetools.sunrise.myaccount.customerprofile;

import io.sphere.sdk.customers.CustomerName;
import io.sphere.sdk.models.Base;
import play.data.validation.Constraints;

public class DefaultCustomerProfileFormData extends Base implements CustomerProfileFormData {

    private String title;

    @Constraints.Required
    private String firstName;

    @Constraints.Required
    private String lastName;

    @Constraints.Required
    private String email;

    @Override
    public CustomerName customerName() {
        return CustomerName.ofTitleFirstAndLastName(title, firstName, lastName);
    }

    @Override
    public String email() {
        return email;
    }
}

