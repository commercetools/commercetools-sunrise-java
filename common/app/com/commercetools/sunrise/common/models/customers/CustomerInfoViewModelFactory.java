package com.commercetools.sunrise.common.models.customers;

import com.commercetools.sunrise.common.models.ViewModelFactory;
import io.sphere.sdk.customers.Customer;

public class CustomerInfoViewModelFactory extends ViewModelFactory<CustomerInfoViewModel, Customer> {

    @Override
    protected CustomerInfoViewModel getViewModelInstance() {
        return new CustomerInfoViewModel();
    }

    @Override
    public final CustomerInfoViewModel create(final Customer data) {
        return super.create(data);
    }

    @Override
    protected final void initialize(final CustomerInfoViewModel model, final Customer data) {
        fillCustomer(model, data);
    }

    protected void fillCustomer(final CustomerInfoViewModel model, final Customer customer) {
        model.setCustomer(customer);
    }
}
