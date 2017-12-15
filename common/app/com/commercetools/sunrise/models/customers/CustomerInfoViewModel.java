package com.commercetools.sunrise.models.customers;

import com.commercetools.sunrise.core.viewmodels.ViewModel;
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
