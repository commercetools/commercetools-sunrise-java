package com.commercetools.sunrise.common.models.customers;

import com.commercetools.sunrise.common.models.ViewModel;
import io.sphere.sdk.customers.Customer;

public class CustomerInfoViewModel extends ViewModel {

    private Customer customer;

    public CustomerInfoViewModel() {
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(final Customer customer) {
        this.customer = customer;
    }
}
