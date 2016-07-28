package com.commercetools.sunrise.myaccount.mydetails;

import com.commercetools.sunrise.common.models.ModelBean;
import io.sphere.sdk.customers.Customer;

public class CustomerInfoBean extends ModelBean {

    private Customer customer;

    public CustomerInfoBean() {
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(final Customer customer) {
        this.customer = customer;
    }
}
