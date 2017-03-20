package com.commercetools.sunrise.framework.viewmodels.content.customers;

import com.commercetools.sunrise.framework.viewmodels.ViewModel;
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
