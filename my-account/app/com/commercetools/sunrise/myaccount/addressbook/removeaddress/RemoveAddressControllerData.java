package com.commercetools.sunrise.myaccount.addressbook.removeaddress;

import com.commercetools.sunrise.myaccount.EditableCustomerControllerData;
import io.sphere.sdk.customers.Customer;
import play.data.Form;

import javax.annotation.Nullable;

public class RemoveAddressControllerData extends EditableCustomerControllerData<RemoveAddressFormData> {

    public RemoveAddressControllerData(final Form<? extends RemoveAddressFormData> form, final Customer resource, @Nullable final Customer updatedResource) {
        super(form, resource, updatedResource);
    }

    @Override
    public Customer getCustomer() {
        return super.getCustomer();
    }

    @Override
    public Form<? extends RemoveAddressFormData> getForm() {
        return super.getForm();
    }
}
