package com.commercetools.sunrise.myaccount.addressbook.addaddress;

import com.commercetools.sunrise.myaccount.EditableCustomerControllerData;
import com.commercetools.sunrise.myaccount.addressbook.AddressBookAddressFormData;
import io.sphere.sdk.customers.Customer;
import play.data.Form;

import javax.annotation.Nullable;

public class AddAddressControllerData extends EditableCustomerControllerData<AddressBookAddressFormData> {

    public AddAddressControllerData(final Form<? extends AddressBookAddressFormData> form, final Customer resource, @Nullable final Customer updatedResource) {
        super(form, resource, updatedResource);
    }

    @Override
    public Customer getCustomer() {
        return super.getCustomer();
    }

    @Override
    public Form<? extends AddressBookAddressFormData> getForm() {
        return super.getForm();
    }
}
