package com.commercetools.sunrise.myaccount.customerprofile;

import io.sphere.sdk.customers.CustomerName;
import io.sphere.sdk.models.Base;
import play.data.validation.Constraints;

public class DefaultCustomerProfileFormData extends Base implements CustomerProfileFormData {

    public String title;

    @Constraints.Required
    public String firstName;

    @Constraints.Required
    public String lastName;

    @Constraints.Required
    public String email;

    @Override
    public CustomerName customerName() {
        return CustomerName.ofTitleFirstAndLastName(title, firstName, lastName);
    }

    @Override
    public String email() {
        return email;
    }
}

