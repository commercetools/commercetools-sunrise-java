package com.commercetools.sunrise.myaccount.addressbook.addressbookdetail.viewmodels;

import com.commercetools.sunrise.core.viewmodels.content.PageContent;
import io.sphere.sdk.customers.Customer;

public class AddressBookPageContent extends PageContent {

    private Customer customer;

    public AddressBookPageContent() {
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}