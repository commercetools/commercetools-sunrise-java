package com.commercetools.sunrise.myaccount.addressbook.addresslist;

import com.commercetools.sunrise.common.models.ControllerData;
import io.sphere.sdk.customers.Customer;

import javax.annotation.Nullable;

public class AddressBookControllerData extends ControllerData {

    private final Customer customer;

    public AddressBookControllerData(final Customer customer, @Nullable final Customer updatedCustomer) {
        this.customer = updatedCustomer != null ? updatedCustomer : customer;
    }

    public Customer getCustomer() {
        return customer;
    }
}
