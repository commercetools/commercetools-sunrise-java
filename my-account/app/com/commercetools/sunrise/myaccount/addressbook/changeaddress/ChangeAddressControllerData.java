package com.commercetools.sunrise.myaccount.addressbook.changeaddress;

import com.commercetools.sunrise.myaccount.EditableCustomerControllerData;
import com.commercetools.sunrise.myaccount.addressbook.AddressBookAddressFormData;
import io.sphere.sdk.customers.Customer;
import play.data.Form;

import javax.annotation.Nullable;

public class ChangeAddressControllerData extends EditableCustomerControllerData<AddressBookAddressFormData> {

    public ChangeAddressControllerData(final Form<? extends AddressBookAddressFormData> form, final Customer resource, @Nullable final Customer updatedResource) {
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
