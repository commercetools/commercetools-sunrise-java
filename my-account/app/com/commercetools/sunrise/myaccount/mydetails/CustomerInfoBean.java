package com.commercetools.sunrise.myaccount.mydetails;

import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Base;

public class CustomerInfoBean extends Base {

    private Customer customer;

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(final Customer customer) {
        this.customer = customer;
    }
}
