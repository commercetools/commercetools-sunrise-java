package com.commercetools.sunrise.myaccount.common;

import io.sphere.sdk.customers.Customer;

public class MyAccountActionData {

    private final Customer customer;

    public MyAccountActionData(final Customer customer) {
        this.customer = customer;
    }

    public Customer getCustomer() {
        return customer;
    }
}
