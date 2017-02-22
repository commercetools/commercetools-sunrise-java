package com.commercetools.sunrise.common.models.customers;

import com.commercetools.sunrise.common.models.ViewModelFactory;
import io.sphere.sdk.customers.Customer;

public class CustomerInfoViewModelFactory extends ViewModelFactory<CustomerInfoViewModel, Customer> {

    @Override
    protected CustomerInfoViewModel getViewModelInstance() {
        return new CustomerInfoViewModel();
    }

    @Override
    public final CustomerInfoViewModel create(final Customer customer) {
        return super.create(customer);
    }

    @Override
    protected final void initialize(final CustomerInfoViewModel viewModel, final Customer customer) {
        fillCustomer(viewModel, customer);
    }

    protected void fillCustomer(final CustomerInfoViewModel viewModel, final Customer customer) {
        viewModel.setCustomer(customer);
    }
}
