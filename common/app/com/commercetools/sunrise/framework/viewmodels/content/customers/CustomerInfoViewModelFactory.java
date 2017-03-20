package com.commercetools.sunrise.framework.viewmodels.content.customers;

import com.commercetools.sunrise.framework.viewmodels.SimpleViewModelFactory;
import io.sphere.sdk.customers.Customer;

public class CustomerInfoViewModelFactory extends SimpleViewModelFactory<CustomerInfoViewModel, Customer> {

    @Override
    protected CustomerInfoViewModel newViewModelInstance(final Customer customer) {
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
