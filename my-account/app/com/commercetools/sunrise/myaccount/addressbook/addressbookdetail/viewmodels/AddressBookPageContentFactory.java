package com.commercetools.sunrise.myaccount.addressbook.addressbookdetail.viewmodels;

import com.commercetools.sunrise.core.viewmodels.content.PageContentFactory;
import io.sphere.sdk.customers.Customer;

public class AddressBookPageContentFactory extends PageContentFactory<AddressBookPageContent, Customer> {

    @Override
    protected AddressBookPageContent newViewModelInstance(final Customer customer) {
        return new AddressBookPageContent();
    }

    @Override
    public final AddressBookPageContent create(final Customer customer) {
        return super.create(customer);
    }

    @Override
    protected final void initialize(final AddressBookPageContent viewModel, final Customer customer) {
        super.initialize(viewModel, customer);
        fillCustomer(viewModel, customer);
    }

    protected void fillCustomer(final AddressBookPageContent viewModel, final Customer customer) {
        viewModel.setCustomer(customer);
    }
}